package com.hfad.foodex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.foodex.database.account.AccountDao
import com.hfad.foodex.database.basket.BasketDao

class FoodExViewModelFactory(private val accountDao: AccountDao,
                             private val basketDao: BasketDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodExViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodExViewModel(accountDao,basketDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}