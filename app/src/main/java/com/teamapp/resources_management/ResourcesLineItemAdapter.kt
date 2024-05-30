package com.teamapp.resources_management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.lcs.databinding.ResourceLineItemBinding
import com.teamapp.send_order.Product

class ResourcesLineItemAdapter (
    private val onItemSelected: (item: Product) -> Unit
): ListAdapter<Product, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResLnItmViewHolder(
            ResourceLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemSelected
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ResLnItmViewHolder).bind(getItem(position))
    }

    inner class ResLnItmViewHolder(val binding: ResourceLineItemBinding,
        onItemSelected: (item: Product) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
                resName.text = item.name
                resQty.text = item.quantity.toString()

                resOrderButton.setOnClickListener {
                    onItemSelected(item)
                }
            }
        }

    }


    object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name && oldItem.quantity == newItem.quantity
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name && oldItem.quantity == newItem.quantity
        }
    }
}