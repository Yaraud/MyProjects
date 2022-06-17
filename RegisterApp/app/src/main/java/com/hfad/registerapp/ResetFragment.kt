package com.hfad.registerapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.hfad.registerapp.databinding.FragmentResetBinding
import com.hfad.registerapp.viewmodels.AccountViewModel
import com.hfad.registerapp.viewmodels.AccountViewModelFactory
import com.hfad.registerapp.viewmodels.EmailService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.mail.internet.InternetAddress

class ResetFragment : Fragment() {

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =

                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as RegisterApplication).database.accountDao()
        )
    }

    private var _binding: FragmentResetBinding? = null
    private val binding get() = _binding!!

    private fun setEmailError(error: Boolean) {
        if (error) {
            binding.textInputLayout1.isErrorEnabled = true
            binding.textInputLayout1.error = getString(R.string.wrong_email)
        } else {
            binding.textInputLayout1.isErrorEnabled = false
        }
    }

    private fun setPasswordLengthError(error: Boolean) {
        if (error) {
            binding.textInputLayout2.isErrorEnabled = true
            binding.textInputLayout2.error = getString(R.string.short_password)
        } else {
            binding.textInputLayout2.isErrorEnabled = false
        }
    }

    private fun setPasswordMatchError(error: Boolean) {
        if (error) {
            binding.textInputLayout3.isErrorEnabled = true
            binding.textInputLayout3.error = getString(R.string.wrong_password)
        } else {
            binding.textInputLayout3.isErrorEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.resetBtn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val newPassword = binding.editTextNewPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()

            if (viewModel.getIdByEmail(email) == 0)
                setEmailError(true)
            else {
                setEmailError(false)
                if (newPassword.length < 8)
                    setPasswordLengthError(true)
                else {
                    setPasswordLengthError(false)
                    if (confirmPassword != newPassword)
                        setPasswordMatchError(true)
                    else {
                        setPasswordMatchError(false)

                        val auth = EmailService.UserPassAuthenticator(
                            "stelarik52@gmail.com",
                            "nngypeafumotfjay"
                        )
                        val to = listOf(InternetAddress(email))
                        val from = InternetAddress("stelarik52@gmail.com")
                        val mail = EmailService.Email(
                            auth,
                            to,
                            from,
                            "Reseted password",
                            "Your new password is: $confirmPassword"
                        )
                        val emailService = EmailService("smtp.gmail.com", 587)

                        if (context?.let { it -> isOnline(it) } == true) {
                            GlobalScope.launch {
                                emailService.send(mail)
                            }
                            viewModel.resetPassword(email, confirmPassword)
                            val action = ResetFragmentDirections.actionResetFragmentToLoginFragment("")
                            view.findNavController().navigate(action)
                        }
                        else {
                            val toast = Toast.makeText(context,"Check your internet connection",
                                Toast.LENGTH_SHORT)
                            toast.show()
                        }


                    }
                }
            }
        }
        binding.signUpBtn.setOnClickListener {
            val action = ResetFragmentDirections.actionResetFragmentToRegisterFragment()
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
        binding.editTextNewPassword.text = null
        binding.editTextConfirmPassword.text = null
    }
}