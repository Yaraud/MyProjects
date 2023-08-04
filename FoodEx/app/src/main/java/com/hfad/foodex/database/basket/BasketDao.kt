package com.hfad.foodex.database.basket

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {

    @Query("SELECT * FROM basket")
    fun fullBasket(): Flow<List<Basket>>

    @Query("INSERT INTO basket(image,name,price,amount) VALUES (:image,:name,:price,:amount)")
    fun addItem(image: String,name: String,price: Double,amount: Int)

    @Query("UPDATE basket set amount = amount+1 WHERE name = :name")
    fun increaseAmount(name: String)

    @Query("UPDATE basket set amount = amount-1 WHERE name = :name")
    fun decreaseAmount(name: String)

    @Query("DELETE FROM basket WHERE name = :name")
    fun deleteItem(name: String)

    @Query("DELETE FROM basket")
    fun deleteBasket()

    @Query("SELECT id FROM basket WHERE name = :name")
    fun getId(name: String): Int?

    @Query("SELECT image FROM basket WHERE id = :id")
    fun getImage(id: Int): String?

    @Query("SELECT name FROM basket WHERE id = :id")
    fun getItemName(id: Int): String?

    @Query("SELECT name FROM basket")
    fun getNames(): List<String?>

    @Query("SELECT price FROM basket WHERE id = :id")
    fun getPrice(id: Int): Double?

    @Query("SELECT amount FROM basket WHERE id = :id")
    fun getAmount(id: Int): Int?
}