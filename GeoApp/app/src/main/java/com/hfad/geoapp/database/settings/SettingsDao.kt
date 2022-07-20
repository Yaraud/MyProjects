package com.hfad.geoapp.database.settings

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SettingsDao {

    @Query("SELECT distance FROM settings")
    fun getDistance(): Int?

    @Query("SELECT frequency FROM settings")
    fun getFrequency(): Int

    @Query("INSERT INTO settings(distance,frequency) VALUES(:newDistance,:newFrequency)")
    fun addSettings(newDistance: Int, newFrequency: Int)

    @Query("UPDATE settings set distance = :newDistance, frequency = :newFrequency")
    fun updateSettings(newDistance: Int, newFrequency: Int)
}