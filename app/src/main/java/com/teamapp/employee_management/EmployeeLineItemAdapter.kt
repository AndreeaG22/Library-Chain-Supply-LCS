package com.teamapp.employee_management

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.EmployeeLineItemBinding

class EmployeeLineItemAdapter (
    private val onItemDeleted: (item: String) -> Unit
): ListAdapter<String, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EmpLnItmViewHolder(
            EmployeeLineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EmpLnItmViewHolder).bind(getItem(position))
    }

    inner class EmpLnItmViewHolder(val binding: EmployeeLineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                empName.text = item
                ivDelete.setOnClickListener {
                    onItemDeleted(item)
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
