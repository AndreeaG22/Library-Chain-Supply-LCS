package com.teamapp.client_management

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CursorAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentAddClientBinding
import com.teamapp.client_management.Product
import com.teamapp.lcs.databinding.FragmentAddEmployeeBinding
import java.util.Calendar

class AddClientFragment : Fragment() {

    private var _binding: FragmentAddClientBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddClientViewModel by viewModels()

    private lateinit var addedProductLineItemAdapter: AddedProductLineItemAdapter
    private lateinit var productLineItemAdapter: ProductLineItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        val products = listOf(
            Product("Caiet"),
            Product("Pix"),
            Product("Creion"),
            Product("Radiera"),
            Product("Foarfeca"),
            Product("Creta")
        )
        productLineItemAdapter.submitList(products.toMutableList())
    }

    private fun initUI() {
        // Initialize UI components

        addedProductLineItemAdapter = AddedProductLineItemAdapter { item ->
//            viewModel.removeProduct(item)
        }

        productLineItemAdapter = ProductLineItemAdapter {
            val crtList = productLineItemAdapter.currentList

            val checkedItems = crtList.filter { it.isChecked }
            addedProductLineItemAdapter.submitList(checkedItems.toMutableList())
        }
        binding.productsToAdd.apply {
            adapter = productLineItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addedProducts.apply {
            adapter = addedProductLineItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val calendar = Calendar.getInstance()
        binding.empDatePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            // Update selectedDate and TextView when date is changed
            val selectedDate = "$day/${month + 1}/$year"
            binding.empDate.setText(selectedDate)
        }

        // Set initial date in TextView
        val initialDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(
            Calendar.YEAR)}"
        binding.empDate.setText(initialDate)

        // Make the TextView non-clickable and non-focusable
        binding.empDate.isClickable = false
        binding.empDate.isFocusable = false
    }
}