package com.hfad.foodex

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.hfad.foodex.databinding.BasketItemBinding
import com.hfad.foodex.databinding.ItemItemBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.squareup.picasso.Picasso

class ItemsAdapter(
    private val onItemClicked: (MenuFragment.Item) -> Unit,
    private val context: Context,
    private val viewModel: FoodExViewModel,
    private var items: MutableList<MenuFragment.Item>
) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<MenuFragment.Item>) {
        items = filterList
        notifyDataSetChanged()
    }
    fun clear() {
        val size: Int = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val viewHolder = ItemViewHolder(
            ItemItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context,viewModel
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(items[position])
        }
        return viewHolder

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ItemViewHolder(private val binding: ItemItemBinding,
                         val context: Context?,
                         val viewModel: FoodExViewModel) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MenuFragment.Item) {
            Picasso.with(context).load(item.image).into(binding.image)
            binding.itemName.text = item.name
            binding.itemIng.text = item.ingrs
            binding.intoBasketButton.text = "${item.price} $ to basket"
            binding.intoBasketButton.setOnClickListener {
                if (viewModel.getAccountName() == null)
                    Toast.makeText(context,
                        "You need to log in on account page",
                        Toast.LENGTH_SHORT)
                        .show()
                else
                    if (viewModel.getId(item.name) == null)
                        viewModel.addItem(item.image,item.name,item.price.toDouble(),1)
                    else
                        viewModel.increaseAmount(item.name)
            }

        }
    }
}