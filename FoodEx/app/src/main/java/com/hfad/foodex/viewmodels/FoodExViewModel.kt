package com.hfad.foodex.viewmodels

import androidx.lifecycle.ViewModel
import com.hfad.foodex.database.account.AccountDao
import com.hfad.foodex.database.basket.Basket
import com.hfad.foodex.database.basket.BasketDao
import kotlinx.coroutines.flow.Flow

class FoodExViewModel(private val accountDao: AccountDao,
                      private val basketDao: BasketDao
) : ViewModel() {

    fun addAccount(name: String,phone: String,email: String,address: String) =
        accountDao.addAccount(name,phone,email,address)
    fun updateName(name: String) = accountDao.updateName(name)
    fun updatePhone(phone: String) = accountDao.updatePhone(phone)
    fun updateEmail(email: String) = accountDao.updateEmail(email)
    fun updateAddress(address: String) = accountDao.updateAddress(address)
    fun getId(): Int? = accountDao.getId()
    fun getAccountName(): String? = accountDao.getAccountName()
    fun getPhone(): String? = accountDao.getPhone()
    fun getEmail(): String? = accountDao.getEmail()
    fun getAddress(): String? = accountDao.getAddress()
    fun deleteAccount() = accountDao.deleteAccount()

    fun addItem(image: String,name: String,price: Double,amount: Int) =
        basketDao.addItem(image,name,price,amount)
    fun fullBasket(): Flow<List<Basket>> = basketDao.fullBasket()
    fun increaseAmount(name: String) = basketDao.increaseAmount(name)
    fun decreaseAmount(name: String) = basketDao.decreaseAmount(name)
    fun deleteItem(name: String) = basketDao.deleteItem(name)
    fun deleteBasket() = basketDao.deleteBasket()
    fun getId(name: String): Int? = basketDao.getId(name)
    fun getImage(id: Int): String? = basketDao.getImage(id)
    fun getItemName(id: Int): String? = basketDao.getItemName(id)
    fun getPrice(id: Int): Double? = basketDao.getPrice(id)
    fun getAmount(id: Int): Int? = basketDao.getAmount(id)

}