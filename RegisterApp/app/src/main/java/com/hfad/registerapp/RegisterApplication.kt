package com.hfad.registerapp

import android.app.Application
import com.hfad.registerapp.database.AppDatabase

class RegisterApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

}