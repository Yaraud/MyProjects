package com.hfad.geoapp.database.settings

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings (
    @PrimaryKey(autoGenerate = true) val id:Int,
    @NonNull @ColumnInfo(name = "distance") val distance: Int?,
    @NonNull @ColumnInfo(name = "frequency") val frequency: Int?
)