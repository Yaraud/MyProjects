package com.hfad.geoapp

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.geoapp.databinding.FragmentPointsBinding
import com.hfad.geoapp.viewmodels.PointViewModel
import com.hfad.geoapp.viewmodels.PointViewModelFactory
import kotlinx.coroutines.launch


class PointFragment : Fragment() {

    private val viewModel: PointViewModel by activityViewModels {
        PointViewModelFactory(
            (activity?.application as GeoApplication).database.pointDao(),
            (activity?.application as GeoApplication).database.settingsDao()
        )
    }

    private var _binding: FragmentPointsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private fun deleteAllPointsDialog(){
        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Are you sure you want to delete all points?")
        builder?.setPositiveButton("OK") { _, _ ->
            viewModel.deleteAllPoints()
        }
        builder?.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder?.show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPointsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Points"

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_all_items -> {
                        deleteAllPointsDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.bottomNavigation.selectedItemId = R.id.points_item
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.map_item -> {
                    val action = PointFragmentDirections.actionPointsFragmentToMapsFragment(0.0F,0.0F)
                    view.findNavController().navigate(action)
                    true
                }
                R.id.settings_item -> {
                    val action = PointFragmentDirections.actionPointsFragmentToSettingsFragment()
                    view.findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val pointAdapter = PointAdapter( {
            val action =
                PointFragmentDirections.actionPointsFragmentToMapsFragment(
                    latitude = it.latitude.toFloat(),
                    longitude = it.longitude.toFloat()

                )
            view.findNavController().navigate(action)
        },context,viewModel)
        recyclerView.adapter = pointAdapter

        lifecycle.coroutineScope.launch {
            viewModel.allPoints().collect {
                pointAdapter.submitList(it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}