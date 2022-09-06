package com.hfad.buddha

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.buddha.database.quotes.Quote
import com.hfad.buddha.databinding.QuoteItemBinding
import com.hfad.buddha.viewmodels.QuoteViewModel

class QuoteAdapter(
    private val onItemClicked: (Quote) -> Unit,
    private val onLongItemClicked: (String) -> Unit,
    private val viewModel: QuoteViewModel
) : ListAdapter<Quote, QuoteAdapter.QuoteViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Quote>() {
            override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
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
            onLongItemClicked(getItem(position).text)
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
        fun bind(quote: Quote){
            binding.quoteText.text = quote.text
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