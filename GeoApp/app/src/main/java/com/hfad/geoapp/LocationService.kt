package com.hfad.geoapp

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.*

class LocationService : Service() {

    private val binder = LocalBinder()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myLatitude = 0.0
    private var myLongitude = 0.0
    private var frequency: Long = 0
    private val locationRequest: LocationRequest = create().apply {
        interval = frequency
        fastestInterval = frequency
        maxWaitTime = frequency
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                myLatitude = location.latitude
                myLongitude = location.longitude
//                Toast.makeText(this@LocationService, "Latitude: $myLatitude\n" +
//                        "Longitude: $myLongitude", Toast.LENGTH_LONG).show()
//                val intent = Intent(applicationContext,MapsFragment::class.java)
//                intent.putExtra("myLatitude",myLatitude)
//                intent.putExtra("myLongitude",myLatitude)

            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }else{
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
    inner class LocalBinder : Binder() {
        fun getService(): LocationService = this@LocationService
        fun getLat(): Double = myLatitude
        fun getLon(): Double = myLongitude
        fun setFrequency(freq: Long){
            frequency = freq
        }
    }

}