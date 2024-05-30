package com.teamapp.client_management

import android.app.Dialog
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
import com.teamapp.lcs.databinding.ErrorDialogBinding
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
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productLineItemAdapter.submitList(products)
        }
    }
    private fun initUI() {
        // Initialize UI components

        addedProductLineItemAdapter = AddedProductLineItemAdapter { item ->
//            viewModel.removeProduct(item)
        }

        productLineItemAdapter = ProductLineItemAdapter {
            val crtList = productLineItemAdapter.currentList

            val checkedItems = crtList.filter { it.isChecked }
            checkedItems.forEach { it.quantity = 1 }
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
        binding.cltDatePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            // Update selectedDate and TextView when date is changed
            val selectedDate = "$day/${month + 1}/$year"
            binding.cltDate.setText(selectedDate)
        }

        // Set initial date in TextView
        val initialDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(
            Calendar.YEAR)}"
        binding.cltDate.setText(initialDate)

        // Make the TextView non-clickable and non-focusable
        binding.cltDate.isClickable = false
        binding.cltDate.isFocusable = false

        binding.addClientButton.setOnClickListener {
            if (binding.cltAddress.text.toString().isNotEmpty() && binding.cltEmail.text.toString().isNotEmpty()) {
                val address = binding.cltAddress.text.toString()
                val email = binding.cltEmail.text.toString()
                val dateDelivery = binding.cltDate.text.toString()
                val products = addedProductLineItemAdapter.currentList
                viewModel.addClient(address, email, dateDelivery, products)

                requireActivity().supportFragmentManager.popBackStack()
            } else {
                showErrorDialog("Campurile nu pot fi goale")
            }
        }
    }

    private fun showErrorDialog(message: String) {
        Dialog(requireContext()).apply {
            val dialogBinding =
                ErrorDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialogBinding.root)
            dialogBinding.textView.text = message
            dialogBinding.dialogTitle.text = "Eroare"
            dialogBinding.dialogPositiveButton.text = "OK"

            dialogBinding.dialogPositiveButton.setOnClickListener {
                dismiss()
            }
            setCancelable(false)
            show()
        }
    }

}