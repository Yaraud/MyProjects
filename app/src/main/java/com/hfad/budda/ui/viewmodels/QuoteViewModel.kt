package com.hfad.budda.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hfad.budda.database.quotes.Quote
import com.hfad.budda.database.quotes.QuoteDao
import com.hfad.budda.database.settings.SettingsDao
import kotlinx.coroutines.flow.Flow

class QuoteViewModel(private val quoteDao: QuoteDao,
                     private val settingsDao: SettingsDao) : ViewModel() {
    /**
     * Quote
     */
    fun allQuotes(): Flow<List<Quote>> = quoteDao.allQuotes()
    fun addQuote(text: String) = quoteDao.addQuote(text)
    fun numOfQuotes(): Int? = quoteDao.numQuotes()
    fun deleteQuote(text: String) = quoteDao.deleteQuote(text)

    fun getQuoteIds(): IntArray = quoteDao.getQuoteIds()
    fun getQuote(id: Int): String = quoteDao.getQuote(id)

    /**
     * Settings
     */
    fun addSettings(newInterval: Int,newFirstTime: String,newSecondTime: String,newNotify: Boolean,
                    newTheme: Boolean) =
        settingsDao.addSettings(newInterval,newFirstTime,newSecondTime,newNotify,newTheme)
    fun updateInterval(newInterval: Int) = settingsDao.updateInterval(newInterval)
    fun updateFirstTime(newFirstTime: String) = settingsDao.updateFirstTime(newFirstTime)
    fun updateSecondTime(newSecondTime: String) = settingsDao.updateSecondTime(newSecondTime)
    fun updateNotify(newNotify: Boolean) = settingsDao.updateNotify(newNotify)
    fun updateTheme(newTheme: Boolean) = settingsDao.updateTheme(newTheme)
    fun getInterval(): Int? = settingsDao.getInterval()
    fun getFirstTime(): String = settingsDao.getFirstTime()
    fun getSecondTime(): String = settingsDao.getSecondTime()
    fun getNotify(): Boolean? = settingsDao.getNotify()
    fun getTheme(): Boolean? = settingsDao.getTheme()
}