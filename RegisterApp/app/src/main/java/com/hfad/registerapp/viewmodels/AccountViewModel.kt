package com.hfad.registerapp.viewmodels

import androidx.lifecycle.ViewModel
import com.hfad.registerapp.database.account.Account
import com.hfad.registerapp.database.account.AccountDao
import kotlinx.coroutines.flow.Flow

class AccountViewModel(private val accountDao: AccountDao) : ViewModel() {
    fun allAccounts(): Flow<List<Account>> = accountDao.allAccounts()
    fun insertAccount(email: String, password: String) = accountDao.insertAccount(email, password)
    fun resetPassword(currentEmail: String, newPassword: String) =
        accountDao.resetPassword(currentEmail, newPassword)

    fun getIdByEmail(email: String) = accountDao.getIdByEmail(email)
    fun getIdByEmailAndPassword(email: String, password: String) =
        accountDao.getIdByEmailAndPassword(email, password)
}