package com.hfad.foodex

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.foodex.databinding.FragmentBasketBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.hfad.foodex.viewmodels.FoodExViewModelFactory
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class BasketFragment : Fragment() {

    private val viewModel: FoodExViewModel by viewModels {
        FoodExViewModelFactory(
            (activity?.application as FoodExApplication).database.accountDao(),
            (activity?.application as FoodExApplication).database.basketDao()
        )
    }

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater,container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val basketAdapter = context?.let { BasketAdapter({}, it,viewModel) }
        recyclerView.adapter = basketAdapter
        var order = ""
        lifecycle.coroutineScope.launch {
            viewModel.fullBasket().collect {
                order = "Customer: ${viewModel.getAccountName()}\n" +
                        "Phone: ${viewModel.getPhone()}\n" +
                        "Email: ${viewModel.getEmail()}\n" +
                        "Address: ${viewModel.getAddress()}\n\n"
                basketAdapter?.submitList(it)
                if (it.isEmpty()) {
                    binding.orderButton.visibility = View.INVISIBLE
                    binding.textView.visibility = View.VISIBLE
                }
                var fullPrice = 0.0
                for (item in it) {
                    fullPrice += item.price * item.amount
                    order += "${item.name} ${item.amount}x\n"
                }
                order += "\nTotal: ${(fullPrice * 100.0).roundToInt() / 100.0} $"
                binding.orderButton.text = "Order for ${String.format("%.2f", fullPrice)} $"
            }
        }
        binding.orderButton.setOnClickListener {
            if (context?.let { it1 -> isOnline(it1) } == true) {
                val popUp = MoneyPopUpFragment(order)
                popUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
            }
            else
               Toast.makeText(context,"Check your internet connection",
                    Toast.LENGTH_SHORT).show()
        }
    }
}