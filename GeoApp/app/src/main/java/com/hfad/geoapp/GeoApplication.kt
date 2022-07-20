package com.hfad.geoapp

import android.app.Application
import com.hfad.geoapp.database.AppDatabase

class GeoApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

}