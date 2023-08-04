package com.hfad.foodex.database.account

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AccountDao {

    @Query("INSERT INTO account(name,phone,email,address) VALUES (:name,:phone,:email,:address)")
    fun addAccount(name: String,phone: String,email: String,address: String)

    @Query("UPDATE account set name = :name")
    fun updateName(name: String)

    @Query("UPDATE account set phone = :phone")
    fun updatePhone(phone: String)

    @Query("UPDATE account set email = :email")
    fun updateEmail(email: String)

    @Query("UPDATE account set address = :address")
    fun updateAddress(address: String)

    @Query("SELECT id FROM account")
    fun getId(): Int?

    @Query("SELECT name FROM account")
    fun getAccountName(): String?

    @Query("SELECT phone FROM account")
    fun getPhone(): String?

    @Query("SELECT email FROM account")
    fun getEmail(): String?

    @Query("SELECT address FROM account")
    fun getAddress(): String?

    @Query("DELETE FROM account")
    fun deleteAccount()

}
