package com.teamapp.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.lcs.R
import com.teamapp.home.OldLineData
import com.teamapp.lcs.databinding.HomeOldLineItemBinding

class HomeOldLineItemAdapter (
    private val onItemDeleted: (item: OldLineData) -> Unit
): ListAdapter<OldLineData, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeOldLnItmViewHolder(
            HomeOldLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HomeOldLnItmViewHolder).bind(getItem(position))
    }

    inner class HomeOldLnItmViewHolder(val binding: HomeOldLineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OldLineData) {
            binding.apply {
                receiverName.text = item.receiver
                orderCode.text = item.code
                orderedQuantity.text = item.quantity.toString()
                totalSum.text = item.totalSum.toString()
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<OldLineData>() {
        override fun areItemsTheSame(oldItem: OldLineData, newItem: OldLineData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OldLineData, newItem: OldLineData): Boolean {
            return oldItem == newItem
        }
    }
}
