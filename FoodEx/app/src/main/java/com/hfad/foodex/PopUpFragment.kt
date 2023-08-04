package com.hfad.foodex

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.hfad.foodex.databinding.FragmentPopUpBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.hfad.foodex.viewmodels.FoodExViewModelFactory

class PopUpFragment : DialogFragment() {

    private val viewModel: FoodExViewModel by viewModels {
        FoodExViewModelFactory(
            (activity?.application as FoodExApplication).database.accountDao(),
            (activity?.application as FoodExApplication).database.basketDao()
        )
    }

    private var _binding: FragmentPopUpBinding? = null
    private val binding get() = _binding!!

    private fun setNoNameError(error: Boolean) {
        if (error) {
            binding.textInputLayout1.isErrorEnabled = true
            binding.textInputLayout1.error = getString(R.string.no_name)
        } else {
            binding.textInputLayout1.isErrorEnabled = false
        }
    }
    private fun setNoPhoneError(error: Boolean) {
        if (error) {
            binding.textInputLayout2.isErrorEnabled = true
            binding.textInputLayout2.error = getString(R.string.no_phone)
        } else {
            binding.textInputLayout2.isErrorEnabled = false
        }
    }
    private fun setNoAddressError(error: Boolean) {
        if (error) {
            binding.textInputLayout4.isErrorEnabled = true
            binding.textInputLayout4.error = getString(R.string.no_address)
        } else {
            binding.textInputLayout4.isErrorEnabled = false
        }
    }
    private fun setPhoneSyntaxError(error: Boolean) {
        if (error) {
            binding.textInputLayout2.isErrorEnabled = true
            binding.textInputLayout2.error = getString(R.string.wrong_phone_syntax)
        } else {
            binding.textInputLayout2.isErrorEnabled = false
        }
    }
    private fun setEmailSyntaxError(error: Boolean) {
        if (error) {
            binding.textInputLayout3.isErrorEnabled = true
            binding.textInputLayout3.error = getString(R.string.wrong_email_syntax)
        } else {
            binding.textInputLayout3.isErrorEnabled = false
        }
    }
    private fun setAddressSyntaxError(error: Boolean) {
        if (error) {
            binding.textInputLayout4.isErrorEnabled = true
            binding.textInputLayout4.error = getString(R.string.wrong_address_syntax)
        } else {
            binding.textInputLayout4.isErrorEnabled = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopUpBinding.inflate(inflater,container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.getId() != null){
            binding.exitButton.text = "delete\naccount"
            binding.exitButton.setOnClickListener {
                viewModel.deleteAccount()
                viewModel.deleteBasket()
                (activity as MainActivity).addFragment<LogInFragment>()
            }
            binding.editTextName.setText(viewModel.getAccountName())
            binding.editTextPhone.setText(viewModel.getPhone())
            if (viewModel.getEmail() == "–")
                binding.editTextEmail.setText("")
            else
                binding.editTextEmail.setText(viewModel.getEmail())
            binding.editTextAddress.setText(viewModel.getAddress())
        } else {
            binding.exitButton.text = "cancel"
            binding.exitButton.setOnClickListener {
                (activity as MainActivity).addFragment<LogInFragment>()
            }
        }
        binding.confirmButton.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val phone = binding.editTextPhone.text.toString()
            val email = binding.editTextEmail.text.toString()
            val address = binding.editTextAddress.text.toString()
            val pattern = ("\\s*[a-zA-Z0-9а-яА-Я-. ]+\\s*,\\s*[0-9]+[a-zA-Zа-яА-Я-. ]*\\s*,*\\s*[0-9]*[a-zA-Zа-яА-Я-. ]*\\s*").toRegex()
            if (name == "")
                setNoNameError(true)
            else {
                setNoNameError(false)
                if (phone == "")
                    setNoPhoneError(true)
                else {
                    setNoPhoneError(false)
                    if (!android.util.Patterns.PHONE.matcher(phone).matches())
                        setPhoneSyntaxError(true)
                    else {
                        setPhoneSyntaxError(false)
                        if (email == "") {
                            if (address == "")
                                setNoAddressError(true)
                            else {
                                setNoAddressError(false)
                                if (!address.matches(pattern))
                                    setAddressSyntaxError(true)
                                else {
                                    setAddressSyntaxError(false)
                                    if (viewModel.getId() != null) {
                                        viewModel.updateName(name)
                                        viewModel.updatePhone(phone)
                                        viewModel.updateEmail("–")
                                        viewModel.updateAddress(address)
                                    }
                                    viewModel.addAccount(name, phone, "–", address)
                                    (activity as MainActivity).addFragment<AccountFragment>()
                                }
                            }
                        }
                        else {
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                                setEmailSyntaxError(true)
                            else {
                                setEmailSyntaxError(false)
                                if (address == "")
                                    setNoAddressError(true)
                                else {
                                    setNoAddressError(false)
                                    if (!address.matches(pattern))
                                        setAddressSyntaxError(true)
                                    else {
                                        setAddressSyntaxError(false)
                                        if (viewModel.getId() != null) {
                                            viewModel.updateName(name)
                                            viewModel.updatePhone(phone)
                                            viewModel.updateEmail(email)
                                            viewModel.updateAddress(address)
                                        }
                                        viewModel.addAccount(name, phone, email, address)
                                        (activity as MainActivity).addFragment<AccountFragment>()
                                    }
                                }
                            }
                        }
                    }
                }
            }
         }
    }
}