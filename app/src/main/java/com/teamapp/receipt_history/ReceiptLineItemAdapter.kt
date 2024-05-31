package com.teamapp.receipt_history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.ReceiptData
import com.teamapp.lcs.databinding.ReceiptLineItemBinding

class ReceiptLineItemAdapter (
    private val onItemClicked: (item: ReceiptData) -> Unit
): ListAdapter<ReceiptData, RecyclerView.ViewHolder>(DiffCallback) {

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
        fun bind(item: ReceiptData) {
            binding.apply {
                recipientEmail.text = item.email
                dateReceipt.text = item.date
                recipientAddress.text = item.destination
                headerConstraintsLayout.setOnClickListener {
                    // create receipt
                    onItemClicked(item)
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<ReceiptData>() {
        override fun areItemsTheSame(oldItem: ReceiptData, newItem: ReceiptData): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ReceiptData, newItem: ReceiptData): Boolean {
            return oldItem == newItem
        }
    }
}
