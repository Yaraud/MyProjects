package com.hfad.buddha

import android.app.Application
import com.hfad.buddha.database.AppDatabase

class BuddhaApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this)}
}