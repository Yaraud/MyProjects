package com.hfad.foodex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hfad.foodex.database.account.Account
import com.hfad.foodex.database.account.AccountDao
import com.hfad.foodex.database.basket.Basket
import com.hfad.foodex.database.basket.BasketDao

@Database(entities = [Account::class, Basket::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun basketDao(): BasketDao
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