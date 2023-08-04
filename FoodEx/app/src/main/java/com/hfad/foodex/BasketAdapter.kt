package com.hfad.foodex

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hfad.foodex.database.basket.Basket
import com.hfad.foodex.databinding.BasketItemBinding
import com.hfad.foodex.viewmodels.FoodExViewModel
import com.squareup.picasso.Picasso

class BasketAdapter(private val onItemClicked: (Basket) -> Unit,
                    private val context: Context,
                    private val viewModel: FoodExViewModel
    ): ListAdapter<Basket,BasketAdapter.BasketViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Basket>() {
            override fun areItemsTheSame(oldItem: Basket, newItem: Basket): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Basket, newItem: Basket): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val viewHolder = BasketViewHolder(
            BasketItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),viewModel,context
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BasketViewHolder(
        private val binding: BasketItemBinding,
        private val viewModel: FoodExViewModel,
        val context: Context?
    ): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(basket: Basket){
            Picasso.with(context).load(basket.image).into(binding.image)
            binding.itemName.text = basket.name
            binding.itemPrice.text = "${basket.price} $"
            binding.itemAmount.setText(basket.amount.toString())
            binding.addBtn.setOnClickListener {
                viewModel.increaseAmount(basket.name)
                binding.itemAmount.setText(basket.amount.toString())
            }
            binding.removeBtn.setOnClickListener {
                if (basket.amount > 1) {
                    viewModel.decreaseAmount(basket.name)
                    binding.itemAmount.setText(basket.amount.toString())
                } else
                    viewModel.deleteItem(basket.name)
            }
        }
    }
}