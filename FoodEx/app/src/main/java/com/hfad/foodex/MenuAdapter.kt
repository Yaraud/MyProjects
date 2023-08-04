package com.hfad.foodex

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.foodex.viewmodels.FoodExViewModel


class MenuAdapter(
    private val context: Context,
    private var listOfCategories: List<MenuFragment.Category>,
    private val viewModel: FoodExViewModel
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false),
            viewModel
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(listOfCategories[position])
    }

    override fun getItemCount(): Int {
        return listOfCategories.size
    }

    class MenuViewHolder(
        private val view: View,
        private val viewModel: FoodExViewModel
    ) : RecyclerView.ViewHolder(view) {

        lateinit var itemsAdapter: ItemsAdapter

        fun bind(category: MenuFragment.Category) {
            view.findViewById<TextView>(R.id.category_text).text = category.name
            itemsAdapter = ItemsAdapter({
                val intent = Intent(Intent(view.context, ItemActivity::class.java))
                intent.putExtra("image",it.image)
                intent.putExtra("name", it.name)
                intent.putExtra("ingrs", it.ingrs)
                intent.putExtra("price", it.price)
                intent.putExtra("weight", it.weight)
                view.context.startActivity(intent)
            },
            view.context,viewModel,
                category.listOfItems as MutableList<MenuFragment.Item>
            )
            view.findViewById<RecyclerView>(R.id.recycler_view2).adapter = itemsAdapter
        }
    }
}