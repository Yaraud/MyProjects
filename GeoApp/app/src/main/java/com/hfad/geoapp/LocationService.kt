package com.hfad.geoapp

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
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
                Log.d("DEBUG interval","${locationRequest.interval}")
//                Toast.makeText(this@LocationService, "Latitude: $myLatitude\n" +
//                        "Longitude: $myLongitude, ${locationRequest.interval}", Toast.LENGTH_LONG)
//                    .show()

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
            locationRequest.interval=freq
            locationRequest.fastestInterval = freq
            locationRequest.maxWaitTime = freq
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }
}