package com.hfad.geoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hfad.geoapp.database.point.Point
import com.hfad.geoapp.database.point.PointDao
import com.hfad.geoapp.database.settings.Settings
import com.hfad.geoapp.database.settings.SettingsDao

@Database(entities = [Point::class,Settings::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun pointDao(): PointDao
    abstract fun settingsDao(): SettingsDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}