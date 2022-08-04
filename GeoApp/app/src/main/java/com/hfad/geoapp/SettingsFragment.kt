package com.hfad.geoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hfad.geoapp.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val frag: MyPreferenceFragment = childFragmentManager.findFragmentById(R.id.myPreferenceFragment)
//                as MyPreferenceFragment
//        val dist = frag.getDistance()
//        val freq = frag.getFrequency()
//        Toast.makeText(context,"$dist,$freq", Toast.LENGTH_SHORT).show()
        (activity as AppCompatActivity).supportActionBar?.title = "Settings"
        binding.bottomNavigation.selectedItemId = R.id.settings_item
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.map_item -> {
                    val action = SettingsFragmentDirections.actionSettingsFragmentToMapsFragment(0.0F,0.0F)
                    view.findNavController().navigate(action)
                    true
                }
                R.id.points_item -> {
                    val action = SettingsFragmentDirections.actionSettingsFragmentToPointsFragment()
                    view.findNavController().navigate(action)
                    true
                }
                R.id.settings_item -> {
                    true
                }
                else -> false
            }
        }
    }
}