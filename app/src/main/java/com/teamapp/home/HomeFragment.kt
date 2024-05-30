package com.teamapp.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamapp.home.HomeOldLineItemAdapter
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeOldListAdapter: HomeOldLineItemAdapter
    private lateinit var homeFutureListAdapter: HomeFutureLineItemAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModel()

    }
    private fun observeViewModel() {
        viewModel.futureOrders.observe(viewLifecycleOwner) { homeFutureList ->
            homeFutureListAdapter.submitList(homeFutureList)
        }
        viewModel.oldOrders.observe(viewLifecycleOwner) { homeOldList ->
            homeOldListAdapter.submitList(homeOldList)
        }
    }
    private fun initUI() {
        homeOldListAdapter = HomeOldLineItemAdapter { item ->
            // Handle item deletion if needed
        }
        binding.homeOldList.apply {
            adapter = homeOldListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        homeFutureListAdapter = HomeFutureLineItemAdapter { item ->
            // Handle item deletion if needed
        }
        binding.homeFutureList.apply {
            adapter = homeFutureListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}