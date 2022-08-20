package com.hfad.budda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.budda.database.quotes.QuoteDao
import com.hfad.budda.database.settings.SettingsDao

class QuoteViewModelFactory(private val quoteDao: QuoteDao,
                            private val settingsDao: SettingsDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuoteViewModel(quoteDao,settingsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}