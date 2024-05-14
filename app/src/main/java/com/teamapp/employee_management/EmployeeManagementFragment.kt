package com.teamapp.employee_management

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentEmployeeManagementBinding

class EmployeeManagementFragment : Fragment() {

    private var _binding: FragmentEmployeeManagementBinding? = null
    private val binding get() = _binding!!
    private lateinit var empListAdapter: EmployeeLineItemAdapter

    companion object {
        fun newInstance() = EmployeeManagementFragment()
    }

    private val viewModel: EmployeeManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        val empList = listOf("Employee 1", "Employee 2", "Employee 3", "Employee 4", "Employee 5")
        empListAdapter.submitList(empList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        empListAdapter = EmployeeLineItemAdapter { item ->
            // Handle item deletion if needed
        }
        binding.empList.apply {
            adapter = empListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addEmployeeButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddEmployeeFragment())
                .addToBackStack(null)
                .commit()

        }
    }
}
