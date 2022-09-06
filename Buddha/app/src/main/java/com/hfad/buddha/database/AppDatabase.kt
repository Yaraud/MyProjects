package com.hfad.buddha.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hfad.buddha.database.quotes.Quote
import com.hfad.buddha.database.quotes.QuoteDao
import com.hfad.buddha.database.settings.Settings
import com.hfad.buddha.database.settings.SettingsDao

@Database(entities = [Quote::class, Settings::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun quotesDao(): QuoteDao
    abstract fun settingsDao(): SettingsDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context,
                    AppDatabase::class.java,"app_database")
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}