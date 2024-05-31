package com.teamapp.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.lcs.databinding.HomeFutureLineItemBinding

class HomeFutureLineItemAdapter (
    private val onItemDeleted: (item: FutureLineData) -> Unit
): ListAdapter<FutureLineData, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeFutureLnItmViewHolder(
            HomeFutureLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HomeFutureLnItmViewHolder).bind(getItem(position))
    }

    inner class HomeFutureLnItmViewHolder(val binding: HomeFutureLineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FutureLineData) {
            binding.apply {
                receiverName.text = item.receiver
                dateOrder.text = item.date
                qty.text = item.totalQty.toString()
                totalSum.text = item.totalSum.toString()
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<FutureLineData>() {
        override fun areItemsTheSame(oldItem: FutureLineData, newItem: FutureLineData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FutureLineData, newItem: FutureLineData): Boolean {
            return oldItem == newItem
        }
    }
}
