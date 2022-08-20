package com.hfad.budda

import android.app.Application
import com.hfad.budda.database.AppDatabase

class BuddhaApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this)}
}