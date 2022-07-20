package com.hfad.geoapp.database.point

import androidx.room.Dao
import androidx.room.Query
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {
    @Query("SELECT * FROM point")
    fun allPoints(): Flow<List<Point>>

    @Query("INSERT INTO point(name,latitude,longitude,address) VALUES(:name,:latitude,:longitude,:address)")
    fun insertPoint(name: String, latitude: Double, longitude: Double, address: String)

    @Query("UPDATE point SET name = :newName WHERE name = :oldName")
    fun changePoint(oldName: String, newName: String)

    @Query("DELETE FROM point WHERE name = :name")
    fun deletePoint(name: String)

    @Query("DELETE FROM point")
    fun deleteAllPoints()

    @Query("SELECT latitude,longitude FROM point WHERE name = :name")
    fun getCoordinates(name: String): LatLng

    @Query("SELECT name FROM point WHERE id = :id")
    fun getName(id: Int): String?

    @Query("SELECT id FROM point WHERE name = :name")
    fun getId(name: String): Int?

    @Query("SELECT address FROM point WHERE name = :name")
    fun getAddress(name: String): String?
}