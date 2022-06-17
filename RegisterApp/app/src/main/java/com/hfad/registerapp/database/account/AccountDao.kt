package com.hfad.registerapp.database.account

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun allAccounts(): Flow<List<Account>>

    @Query("INSERT INTO account(email,password) VALUES(:email,:password)")
    fun insertAccount(email: String, password: String)

    @Query("UPDATE account SET password = :newPassword WHERE email = :currentEmail")
    fun resetPassword(currentEmail: String, newPassword: String)

    @Query("SELECT id FROM account WHERE email = :email")
    fun getIdByEmail(email: String): Int

    @Query("SELECT id FROM account WHERE email = :email AND password = :password")
    fun getIdByEmailAndPassword(email: String, password: String): Int
}