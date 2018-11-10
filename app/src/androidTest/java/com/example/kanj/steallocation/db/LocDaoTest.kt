package com.example.kanj.steallocation.db

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocDaoTest {

    private lateinit var locDao: LocDao
    private lateinit var db: LocationDb

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(getTargetContext(), LocationDb::class.java).build()
        locDao = db.locDao()
    }

    @Test
    fun getCountSingleTest() {

        Log.v("Kanj", "Single count when db empty")
        locDao.countSingle
                .subscribe(
                        { Log.v("Kanj", "Single count $it") },
                        { Log.v("Kanj", "Single count error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Single count when db has 2 rows")
        locDao.countSingle
                .subscribe(
                        { Log.v("Kanj", "Single count $it") },
                        { Log.v("Kanj", "Single count error ${it.message}") }
                )
    }

    @Test
    fun getCountMaybeTest() {

        Log.v("Kanj", "Maybe count when db empty")
        locDao.countMaybe
                .doOnComplete {
                    Log.v("Kanj", "Maybe count completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Maybe count $it") },
                        { Log.v("Kanj", "Maybe count error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Maybe count when db has 2 rows")
        locDao.countMaybe
                .doOnComplete {
                    Log.v("Kanj", "Maybe count completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Maybe count $it") },
                        { Log.v("Kanj", "Maybe count error ${it.message}") }
                )
    }

    @Test
    fun getCountFlowableTest() {

        Log.v("Kanj", "Subscribe to Flowable count when db empty")
        locDao.countFlowable
                .doOnComplete {
                    Log.v("Kanj", "Flowable count completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Flowable count $it") },
                        { Log.v("Kanj", "Flowable count error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Flowable count add 2 rows in db")

        Thread.sleep(2000)

        Log.v("Kanj", "Flowable count delete all rows from db")

        locDao.clearDb()

        Thread.sleep(2000)
    }

    @Test
    fun getIdListSingleTest() {

        Log.v("Kanj", "Single list when db empty")
        locDao.idListSingle
                .subscribe(
                        { Log.v("Kanj", "Single id list $it") },
                        { Log.v("Kanj", "Single id list error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Single id list when db has 2 rows")
        locDao.idListSingle
                .subscribe(
                        { Log.v("Kanj", "Single id list $it") },
                        { Log.v("Kanj", "Single id list error ${it.message}") }
                )
    }

    @Test
    fun getIdListMaybeTest() {

        Log.v("Kanj", "Maybe id list when db empty")
        locDao.idListMaybe
                .doOnComplete {
                    Log.v("Kanj", "Maybe id list completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Maybe id list $it") },
                        { Log.v("Kanj", "Maybe id list error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Maybe id list when db has 2 rows")
        locDao.idListMaybe
                .doOnComplete {
                    Log.v("Kanj", "Maybe id list completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Maybe id list $it") },
                        { Log.v("Kanj", "Maybe id list error ${it.message}") }
                )
    }

    @Test
    fun getIdListFlowableTest() {

        Log.v("Kanj", "Subscribe to Flowable id list when db empty")
        locDao.idListFlowable
                .doOnComplete {
                    Log.v("Kanj", "Flowable id list completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Flowable id list $it") },
                        { Log.v("Kanj", "Flowable id list error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Flowable id list add 2 rows in db")

        Thread.sleep(2000)

        Log.v("Kanj", "Flowable id list delete all rows from db")

        locDao.clearDb()

        Thread.sleep(2000)
    }

    @Test
    fun getLocationSingleTest() {

        Log.v("Kanj", "Single location when db empty")
        locDao.getLocationSingle(100)
                .subscribe(
                        { Log.v("Kanj", "Single location $it") },
                        { Log.v("Kanj", "Single location error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Single location when db has the queried row")
        locDao.getLocationSingle(100)
                .subscribe(
                        { Log.v("Kanj", "Single location $it") },
                        { Log.v("Kanj", "Single location error ${it.message}") }
                )
    }

    @Test
    fun getLocationMaybeTest() {

        Log.v("Kanj", "Maybe location when db empty")
        locDao.getLocationMaybe(100)
                .doOnComplete {
                    Log.v("Kanj", "Maybe location completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Maybe location $it") },
                        { Log.v("Kanj", "Maybe location error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Maybe location when db has the queried row")
        locDao.getLocationMaybe(100)
                .doOnComplete {
                    Log.v("Kanj", "Maybe location completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Maybe location $it") },
                        { Log.v("Kanj", "Maybe location error ${it.message}") }
                )
    }

    @Test
    fun getLocationFlowableTest() {

        Log.v("Kanj", "Subscribe to Flowable location when db empty")
        locDao.getLocationFlowable(100)
                .doOnComplete {
                    Log.v("Kanj", "Flowable location completed")
                }
                .subscribe(
                        { Log.v("Kanj", "Flowable location $it") },
                        { Log.v("Kanj", "Flowable location error ${it.message}") }
                )

        Thread.sleep(2000)

        locDao.insertLocations(TWO_LOCATIONS)

        Log.v("Kanj", "Flowable location add 2 rows in db")

        Thread.sleep(2000)

        Log.v("Kanj", "Flowable location delete all rows from db")

        locDao.clearDb()

        Thread.sleep(2000)
    }

    @After
    fun closeDb() {
        db.close()
    }

    companion object {
        val TWO_LOCATIONS = arrayListOf(
                Loc(23.56, 78.96, 7, 12345678).apply { id = 100 },
                Loc(23.57, 78.97, 5, 12345688)
        )
    }
}
