package com.teamapp.send_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.lcs.databinding.AddedProductLineItemBinding

class AddedProductLineItemAdapter (
private val onQtyChanged: (item: Product) -> Unit,
//private val onQtyIncreased: (item: Product) -> Boolean
): ListAdapter<Product, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddedProductLnItmViewHolder(
            AddedProductLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AddedProductLnItmViewHolder).bind(getItem(position))
    }

    inner class AddedProductLnItmViewHolder(val binding: AddedProductLineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
                prod.text = item.name

                price.visibility = View.VISIBLE
                price.text = item.price.toString()

                qty.visibility = View.VISIBLE
                qty.text = item.quantity.toString()

                minus.visibility = View.VISIBLE
                minus.setOnClickListener {
                    if (item.quantity > 1) {
                        item.quantity--
                        qty.text = item.quantity.toString()
                        onQtyChanged(item)
                    }
                }
                plus.visibility = View.VISIBLE
                plus.setOnClickListener {
                    item.quantity++
                    qty.text = item.quantity.toString()
                    onQtyChanged(item)
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