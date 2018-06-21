package com.example.kanj.steallocation

import android.app.Service
import android.arch.persistence.room.Room
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.example.kanj.steallocation.api.UserService
import com.example.kanj.steallocation.api.pojo.LocationsDetails
import com.example.kanj.steallocation.api.pojo.ReqUserLoc
import com.example.kanj.steallocation.db.Loc
import com.example.kanj.steallocation.db.LocDao
import com.example.kanj.steallocation.db.LocationDb
import com.google.android.gms.location.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class TrackingService : Service() {
    companion object {
        val DISTANCE_THRESHOLD_IN_METRES = 100
        val TIME_THRESHOLD_IN_MILLIS = 2 * 60 * 1000
    }

    private var uploadDisposable: Disposable? = null

    private lateinit var locHandler: LocCallbackHandler
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locDb: LocDao
    private lateinit var userService: UserService

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        locDb = Room.databaseBuilder(this.applicationContext, LocationDb::class.java, "locations")
                .build()
                .locDao()
        val deleter = object : Thread() {
            override fun run() {
                locDb.clearDb()
            }
        }
        deleter.start()

        userService = UserService.INSTANCE
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            locationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    locHandler = LocCallbackHandler(it.latitude, it.longitude, it.time)
                } else {
                    locHandler = LocCallbackHandler(90.00, 0.0, 0)
                }
                startTrackingLocation()
            }
        } catch (e: SecurityException) {
            // MainActivity asks for permission relentlessly. Can't start service without permission.
            e.printStackTrace()
        }
        return Service.START_STICKY
    }

    private fun startTrackingLocation() {
        try {
            val locationRequest = LocationRequest().apply {
                interval = MainActivity.INTERVAL
                fastestInterval = MainActivity.INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            locationClient.requestLocationUpdates(locationRequest, locHandler, null)
        } catch (e: SecurityException) {
            // MainActivity asks for permission relentlessly. Can't start service without permission.
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient.removeLocationUpdates(locHandler)
        uploadDisposable?.dispose()
    }

    inner class LocCallbackHandler(var lastLat: Double, var lastLon: Double, var lastTime: Long) : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result ?: return
            val locs = ArrayList<Loc>()
            for (location in result.locations) {
                val distance = floatArrayOf(0f)
                Location.distanceBetween(lastLat, lastLon, location.latitude, location.longitude, distance)
                if (distance[0] >= DISTANCE_THRESHOLD_IN_METRES || location.time - lastTime >= TIME_THRESHOLD_IN_MILLIS) {
                    locs.add(Loc(location.latitude, location.longitude, location.accuracy.toInt()))
                    lastLat = location.latitude
                    lastLon = location.longitude
                    lastTime = location.time
                }
            }
            if (locs.size > 0) {
                saveAndTryToUpload(locs)
            }
        }
    }

    private fun saveAndTryToUpload(locs: ArrayList<Loc>) {
        Observable.just(locs)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    locDb.insertLocations(locs)
                    uploadLocs()
                }, {
                    it.printStackTrace()
                })
    }

    private fun uploadLocs() {
        uploadDisposable?.let {
            if (!it.isDisposed) {
                return
            }
        }

        uploadDisposable = locDb.savedLocations
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    tryToUploadOneByOne(it)
                }, {
                    it.printStackTrace()
                    uploadDisposable?.dispose()
                }, {
                    uploadDisposable?.dispose()
                })
    }

    private fun tryToUploadOneByOne(locs: List<Loc>) {
        Observable.fromIterable(locs)
                .flatMapCompletable {
                    Log.v("Kanj", "Try to upload " + it.id)
                    userService.uploadLocation(ReqUserLoc(LocationsDetails(it)))
                            .doOnComplete {
                                Log.v("Kanj", "Deleting " + it.id)
                                locDb.delete(it)
                            }
                            .doOnError {
                                it.printStackTrace()
                            }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    uploadDisposable?.dispose()
                }, {
                    it.printStackTrace()
                })

    }

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented")
    }
}