package com.hfad.registerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.hfad.registerapp.databinding.FragmentRegisterBinding
import com.hfad.registerapp.viewmodels.AccountViewModel
import com.hfad.registerapp.viewmodels.AccountViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as RegisterApplication).database.accountDao()
        )
    }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private fun setEmailSyntaxError(error: Boolean) {
        if (error) {
            binding.textInputLayout1.isErrorEnabled = true
            binding.textInputLayout1.error = getString(R.string.wrong_email_syntax)
        } else {
            binding.textInputLayout1.isErrorEnabled = false
        }
    }

    private fun setEmailRepeatError(error: Boolean) {
        if (error) {
            binding.textInputLayout1.isErrorEnabled = true
            binding.textInputLayout1.error = getString(R.string.repeat_email)
        } else {
            binding.textInputLayout1.isErrorEnabled = false
        }
    }

    private fun setPasswordError(error: Boolean) {
        if (error) {
            binding.textInputLayout2.isErrorEnabled = true
            binding.textInputLayout2.error = getString(R.string.short_password)
        } else {
            binding.textInputLayout2.isErrorEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInBtn.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment("")
            view.findNavController().navigate(action)
        }

        binding.signUpBtn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                setEmailSyntaxError(true)
            else {
                setEmailSyntaxError(false)
                if (viewModel.getIdByEmail(email) != 0)
                    setEmailRepeatError(true)
                else {
                    setEmailRepeatError(false)
                    if (password.length < 8)
                        setPasswordError(true)
                    else {
                        setPasswordError(false)
                        viewModel.insertAccount(email, password)
                        val action =
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(email)
                        view.findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        binding.editTextEmail.text = null
        binding.editTextPassword.text = null

    }

}