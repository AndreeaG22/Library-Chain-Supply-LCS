package com.teamapp.employee_management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.teamapp.lcs.databinding.EmployeeLineItemBinding

class EmployeeLineItemAdapter (
    private val onItemDeleted: (item: Employee) -> Unit
): ListAdapter<Employee, RecyclerView.ViewHolder>(DiffCallback) {

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
        fun bind(item: Employee) {
            binding.apply {
                empName.text = item.name
                empEmail.text = item.email
                ivDelete.setOnClickListener {
                    onItemDeleted(item)
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }
}
