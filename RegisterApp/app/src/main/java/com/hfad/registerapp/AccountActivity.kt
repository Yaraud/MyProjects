package com.hfad.registerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val email = intent?.extras?.getString("email").toString()
        val welcomeTextView = findViewById<TextView>(R.id.welcomeText)
        welcomeTextView.text = getString(R.string.com_hfad_registerapp_welcome_text,email)

    }
}