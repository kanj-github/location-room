package com.example.kanj.steallocation

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        val REQUEST_LOCATION_PERMISSION = 101
        val REQUEST_LOCATION_ENABLE = 102
        val INTERVAL = 6000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateButtons()

        bt_start.setOnClickListener {
            checkLocationEnabledAndStartTracking()
        }
        bt_stop.setOnClickListener {
            stopService(Intent(this, TrackingService::class.java))
            updateButtons()
        }
    }

    private fun updateButtons() {
        val trackingOn = isTrackingOn()
        bt_start.isEnabled = !trackingOn
        bt_stop.isEnabled = trackingOn
    }

    private fun isTrackingOn(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // Even though deprecated, it will return the services of this app as per javadoc
        val services = am.getRunningServices(Int.MAX_VALUE)
        for (serviceInfo in services) {
            if (TrackingService::class.java.name.equals(serviceInfo.service.className)) {
                return true
            }
        }

        return false
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private fun startTrackingService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startService(Intent(this, TrackingService::class.java))
            updateButtons()
        } else {
            Toast.makeText(this, R.string.err_no_access, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkLocationEnabledAndStartTracking() {
        val locationRequest = LocationRequest().apply {
            interval = INTERVAL
            fastestInterval = INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            startTrackingService()
        }
        task.addOnFailureListener {
            if (it is ResolvableApiException){
                try {
                    it.startResolutionForResult(this@MainActivity, REQUEST_LOCATION_ENABLE)
                } catch (e: IntentSender.SendIntentException) {
                    // Ignore
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // No need to check result because onResume will be called again anyway
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION_ENABLE && resultCode == Activity.RESULT_OK) {
            startTrackingService()
        }
    }
}
