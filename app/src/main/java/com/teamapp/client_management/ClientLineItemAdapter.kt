package com.teamapp.client_management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.lcs.databinding.ClientLineItemBinding

class ClientLineItemAdapter (
    private val onItemDeleted: (item: Client) -> Unit
): ListAdapter<Client, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClientLnItmViewHolder(
            ClientLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ClientLnItmViewHolder).bind(getItem(position))
    }

    inner class ClientLnItmViewHolder(val binding: ClientLineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Client) {
            binding.apply {
                clientName.text = item.email
                clientAddress.text = item.address
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
            return oldItem == newItem
        }
    }
}
