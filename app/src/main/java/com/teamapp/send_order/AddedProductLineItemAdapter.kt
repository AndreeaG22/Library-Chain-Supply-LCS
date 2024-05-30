package com.teamapp.send_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
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

    fun getMaxQty(item: Product, callback: (Int) -> Unit) {
        val myRef = FirebaseDatabase.getInstance().getReference("Products")
        var maxQty = 0

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null && product.name == item.name) {
                        maxQty = product.quantity
                    }
                }
                callback(maxQty)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Database error: ${databaseError.message}")
                callback(maxQty)
            }
        })
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
                    getMaxQty(item) { maxQty ->
                        if (item.quantity < maxQty) {
                            item.quantity++
                            qty.text = item.quantity.toString()
                            onQtyChanged(item)
                        }
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