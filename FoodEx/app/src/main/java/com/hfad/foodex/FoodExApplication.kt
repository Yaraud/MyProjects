package com.hfad.foodex

import android.app.Application
import com.hfad.foodex.database.AppDatabase

class FoodExApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this)}
}