package com.teamapp.receipt_history

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamapp.client_management.ClientLineItemAdapter
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentClientManagementBinding

class ReceiptHistoryFragment : Fragment() {
    private var _binding: FragmentReceiptHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var clientListAdapter: ReceiptLineItemAdapter

    companion object {
        fun newInstance() = ReceiptHistoryFragment()
    }

    private val viewModel: ReceiptHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        clientListAdapter = ReceiptLineItemAdapter { item ->
            // Handle item deletion if needed
        }
        binding.receiptList.apply {
            adapter = clientListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addReceiptButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddReceiptFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}