package com.hfad.buddha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.hfad.buddha.databinding.ActivityFullQuoteBinding
import com.hfad.buddha.viewmodels.QuoteViewModel
import com.hfad.buddha.viewmodels.QuoteViewModelFactory

class FullQuoteActivity : AppCompatActivity() {

    private val viewModel: QuoteViewModel by viewModels {
        QuoteViewModelFactory(
            (application as BuddhaApplication).database.quotesDao(),
            (application as BuddhaApplication).database.settingsDao()
        )
    }

    private lateinit var binding: ActivityFullQuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.getTheme() == true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityFullQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.textView.text = intent.getStringExtra("quote")
    }
}