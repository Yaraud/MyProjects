package com.hfad.foodex

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.hfad.foodex.databinding.ActivityItemBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.hfad.foodex.viewmodels.FoodExViewModelFactory
import com.squareup.picasso.Picasso

class ItemActivity : AppCompatActivity() {

    private val viewModel: FoodExViewModel by viewModels {
        FoodExViewModelFactory(
            (application as FoodExApplication).database.accountDao(),
            (application as FoodExApplication).database.basketDao()
        )
    }
    private lateinit var binding: ActivityItemBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val image = intent.getStringExtra("image")
        val price = intent.getStringExtra("price")
        binding.itemName.text = name
        binding.itemWeight.text = intent.getStringExtra("weight")
        binding.itemIng.text = intent.getStringExtra("ingrs")
        Picasso.with(this).load(image)
            .into(findViewById<ImageView>(R.id.image))
        binding.intoBasketButton.text = "To basket: $price $"
        binding.intoBasketButton.setOnClickListener {
            if (viewModel.getAccountName() == null)
                Toast.makeText(this,
                    "You need to log in on account page",
                    Toast.LENGTH_SHORT)
                    .show()
            else
                if (viewModel.getId(name!!) == null) {
                    viewModel.addItem(image!!, name, price!!.toDouble(), 1)
                    finish()
                }
                else {
                    viewModel.increaseAmount(name)
                    finish()
                }
        }
    }
}