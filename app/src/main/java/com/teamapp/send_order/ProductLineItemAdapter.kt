package com.teamapp.send_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.lcs.databinding.ProductLineItemBinding

class ProductLineItemAdapter (
    private val onItemUpdated: () -> Unit,
): ListAdapter<Product, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductLnItmViewHolder(
            ProductLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductLnItmViewHolder).bind(getItem(position))
    }

    inner class ProductLnItmViewHolder(val binding: ProductLineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
                prod.text = item.name

                price.visibility = View.VISIBLE
                price.text = item.price.toString()

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // set the item to checked
                        item.isChecked = true
                        onItemUpdated()
                    } else {
                        // set the item to unchecked
                        item.isChecked = false
                        onItemUpdated()
                    }
                }

            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }
    }
}