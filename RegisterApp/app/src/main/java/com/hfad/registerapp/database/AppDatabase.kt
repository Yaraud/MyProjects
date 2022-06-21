package com.hfad.registerapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hfad.registerapp.database.account.Account
import com.hfad.registerapp.database.account.AccountDao

@Database(entities = [Account::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract  fun accountDao(): AccountDao
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