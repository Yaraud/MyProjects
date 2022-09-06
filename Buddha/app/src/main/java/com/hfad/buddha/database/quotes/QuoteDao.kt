package com.hfad.buddha.database.quotes

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quote")
    fun allQuotes(): Flow<List<Quote>>

    @Query("INSERT INTO quote(text) VALUES (:text)")
    fun addQuote(text: String)

    @Query("SELECT count(*) FROM quote")
    fun numQuotes(): Int?

    @Query("DELETE FROM quote WHERE text = :text")
    fun deleteQuote(text: String)

    @Query("SELECT id FROM quote")
    fun getQuoteIds(): IntArray

    @Query("SELECT text FROM quote WHERE id = :id")
    fun getQuote(id: Int): String
}