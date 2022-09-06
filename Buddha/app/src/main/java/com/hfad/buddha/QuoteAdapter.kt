package com.hfad.buddha

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.buddha.database.quotes.Quote
import com.hfad.buddha.databinding.QuoteItemBinding
import com.hfad.buddha.viewmodels.QuoteViewModel
import java.io.File

class QuoteAdapter(
    private val onItemClicked: (String) -> Unit,
    private val onLongItemClicked: (String) -> Unit,
    private val viewModel: QuoteViewModel
) : ListAdapter<String, QuoteAdapter.QuoteViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val viewHolder = QuoteViewHolder(
            QuoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),viewModel
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        viewHolder.itemView.setOnLongClickListener {
            val position = viewHolder.adapterPosition
            onLongItemClicked(getItem(position))
            return@setOnLongClickListener true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class QuoteViewHolder(
        private val binding: QuoteItemBinding,
        private val viewModel: QuoteViewModel
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(quote: String){
            binding.quoteText.text = quote
            if(viewModel.getTheme() == true) {
                binding.layout.setBackgroundResource(R.drawable.layout_dark_border)
                binding.quoteText.setTextAppearance(R.style.MainLightText)
            } else {
                binding.layout.setBackgroundResource(R.drawable.layout_light_border)
                binding.quoteText.setTextAppearance(R.style.MainText)
            }

        }
    }
}