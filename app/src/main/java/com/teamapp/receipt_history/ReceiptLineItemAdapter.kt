package com.teamapp.receipt_history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.lcs.databinding.ReceiptLineItemBinding

class ReceiptLineItemAdapter (
    private val onItemDeleted: (item: String) -> Unit
): ListAdapter<String, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReceiptLnItmViewHolder(
            ReceiptLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReceiptLnItmViewHolder).bind(getItem(position))
    }

    inner class ReceiptLnItmViewHolder(val binding: ReceiptLineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                receiptName.text = item
                dateReceipt.text = "12/12/2021"
                ivIcon.setOnClickListener {
                    // create receipt
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
