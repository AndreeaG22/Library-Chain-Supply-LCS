package com.teamapp.employee_management

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentEmployeeManagementBinding

class EmployeeManagementFragment : Fragment() {

    private var _binding: FragmentEmployeeManagementBinding? = null
    private val binding get() = _binding!!
    private lateinit var empListAdapter: EmployeeLineItemAdapter

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

        viewModel.employees.observe(viewLifecycleOwner, Observer {
            empListAdapter.submitList(it)
        })

        viewModel.iterateDatabaseItems(Firebase.database.reference.child("employees"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        empListAdapter = EmployeeLineItemAdapter { item ->
            // Handle item deletion if needed
            viewModel.deleteEmployee(Firebase.database.reference, item)
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
