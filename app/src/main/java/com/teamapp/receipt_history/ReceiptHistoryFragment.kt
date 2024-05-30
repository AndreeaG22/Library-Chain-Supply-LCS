package com.teamapp.receipt_history

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentReceiptHistoryBinding

class ReceiptHistoryFragment : Fragment() {
    private var _binding: FragmentReceiptHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var receiptListAdapter: ReceiptLineItemAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel.getReceipts()
        viewModel.receipts.observe(viewLifecycleOwner) { receipts ->
            receiptListAdapter.submitList(receipts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        receiptListAdapter = ReceiptLineItemAdapter { item ->
            // Handle item deletion if needed
        }
        binding.receiptList.apply {
            adapter = receiptListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}