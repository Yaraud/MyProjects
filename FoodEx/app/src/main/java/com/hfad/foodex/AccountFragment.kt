package com.hfad.foodex

import android.annotation.SuppressLint
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.hfad.foodex.databinding.FragmentAccountBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.hfad.foodex.viewmodels.FoodExViewModelFactory
import java.util.*


class AccountFragment : Fragment() {

    private val viewModel: FoodExViewModel by viewModels {
        FoodExViewModelFactory(
            (activity?.application as FoodExApplication).database.accountDao(),
            (activity?.application as FoodExApplication).database.basketDao()
        )
    }

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater,container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.getId() != null) {
            binding.mainLabel.text = "Welcome, ${viewModel.getAccountName()}"
            binding.phoneView.text = PhoneNumberUtils.formatNumber(viewModel.getPhone(),
                Locale.getDefault().country)
            binding.emailView.text = viewModel.getEmail()
            binding.addressView.text = viewModel.getAddress()
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.settings_menu, menu)
                menu.findItem(R.id.settings_item)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val popUp = PopUpFragment()
                popUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}