package com.hfad.geoapp.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.hfad.geoapp.database.point.Point
import com.hfad.geoapp.database.point.PointDao
import com.hfad.geoapp.database.settings.SettingsDao
import kotlinx.coroutines.flow.Flow

class PointViewModel(private val pointDao: PointDao,private val settingsDao: SettingsDao)
    : ViewModel() {
    fun allPoints(): Flow<List<Point>> = pointDao.allPoints()
    fun insertPoint(name: String, latitude: Double, longitude: Double, address: String) =
        pointDao.insertPoint(name, latitude, longitude, address)
    fun changePoint(oldName: String, newName: String) = pointDao.changePoint(oldName, newName)
    fun deletePoint(name: String) = pointDao.deletePoint(name)
    fun deleteAllPoints() = pointDao.deleteAllPoints()
    fun getCoordinates(name: String): LatLng = pointDao.getCoordinates(name)
    fun getId(name: String): Int? = pointDao.getId(name)

    fun getDistance(): Int? = settingsDao.getDistance()
    fun getFrequency(): Int = settingsDao.getFrequency()
    fun addSettings(newDistance: Int, newFrequency: Int) =
        settingsDao.addSettings(newDistance,newFrequency)
    fun updateSettings(newDistance: Int, newFrequency: Int) =
        settingsDao.updateSettings(newDistance,newFrequency)

}