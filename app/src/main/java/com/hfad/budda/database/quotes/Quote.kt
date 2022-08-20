package com.hfad.budda.database.quotes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quote(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "text") val text: String
)