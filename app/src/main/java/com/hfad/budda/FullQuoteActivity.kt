package com.hfad.budda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hfad.budda.databinding.ActivityFullQuoteBinding



class FullQuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullQuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.textView.text = intent.getStringExtra("quote")
    }
}