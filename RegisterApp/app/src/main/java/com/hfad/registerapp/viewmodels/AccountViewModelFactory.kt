package com.hfad.registerapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.registerapp.database.account.AccountDao


class AccountViewModelFactory(
    private val accountDao: AccountDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(accountDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
