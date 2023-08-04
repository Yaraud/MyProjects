package com.hfad.foodex

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.add
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.hfad.foodex.databinding.ActivityMainBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.hfad.foodex.viewmodels.FoodExViewModelFactory
import java.io.File

class MainActivity : AppCompatActivity() {

    private val viewModel: FoodExViewModel by viewModels {
        FoodExViewModelFactory(
            (application as FoodExApplication).database.accountDao(),
            (application as FoodExApplication).database.basketDao()
        )
    }

    private lateinit var binding: ActivityMainBinding
    lateinit var storage: FirebaseStorage
    private lateinit var menuFragment: MenuFragment
    inline fun <reified T> addFragment() where T : Fragment {
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<T>(R.id.fragment)
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = Firebase.storage
        val storageRef = storage.reference
        val menuRef = storageRef.child("menu.txt")
        val localFile = File.createTempFile("menu", "txt")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.selectedItemId = R.id.menu_item
        binding.button2.setOnClickListener {
            if (this.isOnline(this)) {
                binding.checkLayout.visibility = View.INVISIBLE
                menuRef.getFile(localFile).addOnSuccessListener {
                    menuFragment = MenuFragment(localFile)
                    for (fragment in supportFragmentManager.fragments) {
                        supportFragmentManager.beginTransaction().remove(fragment).commit()
                    }
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add(R.id.fragment, menuFragment)
                    }
                }
            } else {
                menuFragment = MenuFragment(localFile)
                for (fragment in supportFragmentManager.fragments) {
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add(R.id.fragment, menuFragment)
                }
                binding.checkLayout.visibility = View.VISIBLE
            }
        }
        if (this.isOnline(this))
            menuRef.getFile(localFile).addOnSuccessListener {
                menuFragment = MenuFragment(localFile)
                for (fragment in supportFragmentManager.fragments) {
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add(R.id.fragment, menuFragment)
                }
            }
        else {
            menuFragment = MenuFragment(localFile)
            for (fragment in supportFragmentManager.fragments) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment, menuFragment)
            }
            binding.checkLayout.visibility = View.VISIBLE
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item -> {
                    if (this.isOnline(this)) {
                        binding.checkLayout.visibility = View.INVISIBLE
                        menuRef.getFile(localFile).addOnSuccessListener {
                            menuFragment = MenuFragment(localFile)
                            Log.d("succ", supportFragmentManager.fragments.size.toString())
                            run breaking@{
                                for (fragment in supportFragmentManager.fragments) {
                                    if (fragment is MenuFragment)
                                        return@breaking
                                    supportFragmentManager.beginTransaction().remove(fragment)
                                        .commit()
                                }
                                supportFragmentManager.commit {
                                    Log.d("succ", supportFragmentManager.fragments.size.toString())
                                    setReorderingAllowed(true)
                                    add(R.id.fragment, menuFragment)
                                }
                            }
                        }
                    } else {
                        menuFragment = MenuFragment(localFile)
                        for (fragment in supportFragmentManager.fragments) {
                            supportFragmentManager.beginTransaction().remove(fragment).commit()
                        }
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add(R.id.fragment, menuFragment)
                        }
                        binding.checkLayout.visibility = View.VISIBLE
                    }
                    true
                }
                R.id.account_item -> {
                    binding.checkLayout.visibility = View.INVISIBLE
                    if (viewModel.getAccountName() == null)
                        addFragment<LogInFragment>()
                    else
                        addFragment<AccountFragment>()
                    true
                }
                R.id.basket_item -> {
                    binding.checkLayout.visibility = View.INVISIBLE
                    addFragment<BasketFragment>()
                    true
                }
                else -> false
            }
        }
    }
}