package com.hfad.foodex

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.google.android.material.tabs.TabLayout
import com.hfad.foodex.databinding.FragmentMenuBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.hfad.foodex.viewmodels.FoodExViewModelFactory
import java.io.File


class MenuFragment(private val localFile: File) : Fragment() {

    private val viewModel: FoodExViewModel by viewModels {
        FoodExViewModelFactory(
            (activity?.application as FoodExApplication).database.accountDao(),
            (activity?.application as FoodExApplication).database.basketDao()
        )
    }

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var tabLayout: TabLayout
    private var items = mutableListOf<Item>()
    private var categoryItems = mutableListOf<Item>()
    private var categories = mutableListOf<Category>()
    lateinit var adapter: MenuAdapter
    lateinit var itemsAdapter: ItemsAdapter

    class Item(
        val image: String,
        val name: String,
        val weight: String,
        val ingrs: String,
        val price: String
    )

    class Category(val name: String, val listOfItems: List<Item>)

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
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context?.let { it1 -> isOnline(it1) } == true) {
            val splitLines = localFile.readText().split("\n")
            var category = ""
            var k = 0
            var i = -2
            for (line in splitLines) {
                val splitCategoryAndItem = line.split("|")

                val itemFields = splitCategoryAndItem[1]
                val splitItemFields = itemFields.split(";")
                val item = Item(
                    splitItemFields[4],
                    splitItemFields[0], splitItemFields[1], splitItemFields[2], splitItemFields[3]
                )
                items.add(item)
                categoryItems.add(item)
                i++
                if (splitLines.indexOf(line) == 0) {
                    category = splitCategoryAndItem[0]
                    k = splitLines.indexOf(line)
                }
                if (splitCategoryAndItem[0] != category) {
                    Log.d("success", categoryItems[1].name)
                    categories.add(Category(category, items.slice(k..i)))
                    k = splitLines.indexOf(line)
                    category = splitCategoryAndItem[0]
                }

                if (splitLines.indexOf(line) == splitLines.size - 1) {
                    categories.add(Category(category, items.slice(k until splitLines.size)))
                }

            }

            recyclerView = binding.recyclerView
            tabLayout = binding.tabs
            for (c in categories)
                tabLayout.addTab(tabLayout.newTab().setText(c.name))
            adapter = MenuAdapter(requireContext(), categories, viewModel)
            itemsAdapter = ItemsAdapter({
                val intent = Intent(Intent(context, ItemActivity::class.java))
                intent.putExtra("image", it.image)
                intent.putExtra("name", it.name)
                intent.putExtra("ingrs", it.ingrs)
                intent.putExtra("price", it.price)
                intent.putExtra("weight", it.weight)
                context?.startActivity(intent)
            }, requireContext(), viewModel, items)
            recyclerView.adapter = adapter
            TabbedListMediator(
                recyclerView,
                tabLayout,
                categories.indices.toList()
            ).attach()

            val menuHost: MenuHost = requireActivity()

            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    menuInflater.inflate(R.menu.search_menu, menu)
                    val menuItem = menu.findItem(R.id.search_item)
                    val searchView: SearchView = menuItem.actionView as SearchView
                    menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                            searchView.setQuery("", false)
                            recyclerView.adapter = adapter
                            TabbedListMediator(
                                recyclerView,
                                tabLayout,
                                categories.indices.toList()
                            ).attach()
                            return true
                        }

                        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                            searchView.setQuery("", false)
                            recyclerView.adapter = adapter
                            TabbedListMediator(
                                recyclerView,
                                tabLayout,
                                categories.indices.toList()
                            ).attach()
                            return true
                        }
                    })

                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            val filteredList: ArrayList<Item> = ArrayList()
                            for (item in items) {
                                if (item.name.lowercase().contains(newText.lowercase())) {
                                    filteredList.add(item)
                                }
                            }
                            if (filteredList.isEmpty()) {
                                itemsAdapter.clear()
                                recyclerView.adapter = itemsAdapter
                            } else {
                                itemsAdapter.filterList(filteredList)
                                recyclerView.adapter = itemsAdapter
                            }
                            return true
                        }
                    })
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }
}