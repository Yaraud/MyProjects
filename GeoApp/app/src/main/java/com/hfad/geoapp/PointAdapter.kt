package com.hfad.geoapp

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.geoapp.database.point.Point
import com.hfad.geoapp.databinding.PointItemBinding
import com.hfad.geoapp.viewmodels.PointViewModel


class PointAdapter(
    private val onItemClicked: (Point) -> Unit,
    val context: Context?,
    private val viewModel: PointViewModel
) : ListAdapter<Point, PointAdapter.PointViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Point>() {
            override fun areItemsTheSame(oldItem: Point, newItem: Point): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Point, newItem: Point): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val viewHolder = PointViewHolder(
            PointItemBinding.inflate(
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

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    class PointViewHolder(
        private var binding: PointItemBinding,
        private val viewModel: PointViewModel,
        val context: Context?
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(point: Point) {
            binding.pointName.text = point.name
            binding.address.text = point.address
            binding.renamePointButton.setOnClickListener {
                val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
                builder?.setTitle("Changing point's name")
                val input = EditText(context)
                input.hint = "Enter new name of point ${point.name}"
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder?.setView(input)
                builder?.setPositiveButton("OK") { _, _ ->
                    viewModel.changePoint(point.name,input.text.toString())
                }
                builder?.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                builder?.show()
            }
            binding.deletePointButton.setOnClickListener {
                val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
                builder?.setTitle("Are you sure you want to delete point ${point.name}?")
                builder?.setPositiveButton("OK") { _, _ ->
                    viewModel.deletePoint(point.name)
                }
                builder?.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                builder?.show()
            }
        }
    }

}