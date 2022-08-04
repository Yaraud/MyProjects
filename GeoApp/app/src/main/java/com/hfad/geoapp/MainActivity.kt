package com.hfad.geoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.hfad.geoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var latitude = 0.0F
    private var longitude = 0.0F
    override fun onNewIntent(intent: Intent?) {
        val extras = intent!!.extras
        if (extras != null){
            latitude = extras.getFloat("latitude")
            longitude = extras.getFloat("longitude")

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appBarConfiguration = AppBarConfiguration
            .Builder(R.id.pointsFragment, R.id.settingsFragment, R.id.mapsFragment)
            .build()
        onNewIntent(intent)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bundle = Bundle()
        bundle.putFloat("latitude", latitude)
        bundle.putFloat("longitude", longitude)
        navController.setGraph(R.navigation.nav_graph,bundle)

        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onRestart() {
        super.onRestart()
        onNewIntent(intent)
        val bundle = Bundle()
        bundle.putFloat("latitude", latitude)
        bundle.putFloat("longitude", longitude)
        navController.setGraph(R.navigation.nav_graph,bundle)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}