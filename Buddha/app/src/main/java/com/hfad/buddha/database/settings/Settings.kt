package com.hfad.buddha.database.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "interval") val interval: Int,
    @ColumnInfo(name = "firstTime") val firstTime: String,
    @ColumnInfo(name = "secondTime") val secondTime: String,
    @ColumnInfo(name = "notify") val notify: Boolean,
    @ColumnInfo(name = "darkTheme") val darkTheme: Boolean
)