package com.hfad.geoapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.geoapp.database.point.PointDao
import com.hfad.geoapp.database.settings.SettingsDao


class PointViewModelFactory(
    private val pointDao: PointDao,
    private val settingsDao: SettingsDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PointViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PointViewModel(pointDao,settingsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}