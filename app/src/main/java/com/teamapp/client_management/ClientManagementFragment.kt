package com.teamapp.client_management

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentClientManagementBinding

class ClientManagementFragment : Fragment() {

    private var _binding: FragmentClientManagementBinding? = null
    private val binding get() = _binding!!
    private lateinit var clientListAdapter: ClientLineItemAdapter

    companion object {
        fun newInstance() = ClientManagementFragment()
    }

    private val viewModel: ClientManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.clients.observe(viewLifecycleOwner) { clients ->
            clientListAdapter.submitList(clients)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        clientListAdapter = ClientLineItemAdapter { item ->
            // Handle item deletion if needed
        }
        binding.clientList.apply {
            adapter = clientListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addClientButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddClientFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}