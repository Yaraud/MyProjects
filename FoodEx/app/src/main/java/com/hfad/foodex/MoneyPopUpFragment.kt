package com.hfad.foodex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.hfad.foodex.databinding.FragmentMoneyPopUpBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.hfad.foodex.viewmodels.FoodExViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.mail.internet.InternetAddress


class MoneyPopUpFragment(private var order: String) : DialogFragment() {

    private var _binding: FragmentMoneyPopUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodExViewModel by viewModels {
        FoodExViewModelFactory(
            (activity?.application as FoodExApplication).database.accountDao(),
            (activity?.application as FoodExApplication).database.basketDao()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoneyPopUpBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.radioButton1.isChecked = false
        binding.radioButton2.isChecked = false
        binding.exitButton.setOnClickListener {
            (activity as MainActivity).addFragment<BasketFragment>()
        }
        binding.radioButton1.setOnClickListener {
                binding.radioButton1.isChecked = true
                binding.radioButton2.isChecked = false
        }
        binding.radioButton2.setOnClickListener {
            binding.radioButton2.isChecked = true
            binding.radioButton1.isChecked = false
        }
        binding.confirmButton.setOnClickListener {

            order += if  (binding.radioButton2.isChecked)
                " (with card)" else " (with cash)"
            val auth = EmailService.UserPassAuthenticator(
                "yaraud52@gmail.com",
                "txtujokpyjmamnnc"
            )
            val to = listOf(InternetAddress("yaraud52@gmail.com"))
            val from = InternetAddress("stelarik52@gmail.com")
            val mail = EmailService.Email(
                auth,
                to,
                from,
                "New Order",
                order
            )
            val emailService = EmailService("smtp.gmail.com", 587)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    emailService.send(mail)

                }
                catch (e: Exception){
                    Log.e("errr",e.toString())
                }
                finally {
                    viewModel.deleteBasket()
                    (activity as MainActivity).addFragment<BasketFragment>()
                }

            }
        }
    }


}