package com.hfad.registerapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.hfad.registerapp.databinding.FragmentLoginBinding
import com.hfad.registerapp.databinding.FragmentRegisterBinding
import com.hfad.registerapp.viewmodels.AccountViewModel
import com.hfad.registerapp.viewmodels.AccountViewModelFactory


class LoginFragment : Fragment() {


    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as RegisterApplication).database.accountDao()
        )
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        var EMAIL = "email"
    }

    private lateinit var email: String

    private fun setError(error: Boolean) {
        if (error) {
            binding.textInputLayout1.isErrorEnabled = true
            binding.textInputLayout1.error = getString(R.string.login_error)
            binding.textInputLayout2.isErrorEnabled = true
            binding.textInputLayout2.error = getString(R.string.login_error)
        } else {
            binding.textInputLayout1.isErrorEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(EMAIL).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registeredText.isVisible = email != ""
        binding.editTextEmail.setText(email)

        binding.signUpBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view.findNavController().navigate(action)
        }

        binding.signInBtn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (viewModel.getIdByEmailAndPassword(email, password) == 0)
                setError(true)
            else {
                val intent = Intent(context, AccountActivity::class.java)
                intent.putExtra("email", email)
                context?.startActivity(intent)
            }
        }

        binding.resetBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToResetFragment()
            view.findNavController().navigate(action)
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