package com.hfad.buddha.database.settings

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SettingsDao {

    @Query("INSERT INTO settings(interval,firstTime,secondTime,notify,darkTheme) " +
            "VALUES(:newInterval,:newFirstTime,:newSecondTime,:newNotify,:newTheme)")
    fun addSettings(newInterval: Int, newFirstTime: String, newSecondTime: String,newNotify: Boolean,
                    newTheme: Boolean)

    @Query("UPDATE settings set interval = :newInterval")
    fun updateInterval(newInterval: Int)

    @Query("UPDATE settings set firstTime = :newFirstTime")
    fun updateFirstTime(newFirstTime: String)

    @Query("UPDATE settings set secondTime = :newSecondTime")
    fun updateSecondTime(newSecondTime: String)

    @Query("UPDATE settings set notify = :newNotify")
    fun updateNotify(newNotify: Boolean)

    @Query("UPDATE settings set darkTheme = :newTheme")
    fun updateTheme(newTheme: Boolean)

    @Query("SELECT interval FROM settings")
    fun getInterval(): Int?

    @Query("SELECT firstTime FROM settings")
    fun getFirstTime(): String

    @Query("SELECT secondTime FROM settings")
    fun getSecondTime(): String

    @Query("SELECT notify FROM settings")
    fun getNotify(): Boolean?

    @Query("SELECT darkTheme FROM settings")
    fun getTheme(): Boolean?

}