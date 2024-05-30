package com.teamapp.send_order

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamapp.home.HomeFragment
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentSendOrderBinding
import java.util.Calendar

class SendOrderFragment : Fragment() {

    private var _binding: FragmentSendOrderBinding? = null
    private val binding get() = _binding!!

    private lateinit var addedProductLineItemAdapter: AddedProductLineItemAdapter
    private lateinit var productLineItemAdapter: ProductLineItemAdapter


    companion object {
        fun newInstance() = SendOrderFragment()
    }

    private val viewModel: SendOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendOrderBinding.inflate(inflater, container, false)
        return binding.root
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
        var qty = 0
        var totalPrice = 0.0

        addedProductLineItemAdapter = AddedProductLineItemAdapter { item ->
            val crtList = addedProductLineItemAdapter.currentList

            qty = crtList.sumBy { it.quantity }
            totalPrice = crtList.sumByDouble { it.price * it.quantity.toDouble() }
            binding.textView4.text = totalPrice.toString()
            binding.textView3.text = qty.toString()
        }

        productLineItemAdapter = ProductLineItemAdapter {
            val crtList = productLineItemAdapter.currentList

            val checkedItems = crtList.filter { it.isChecked }
            checkedItems.forEach() {
                it.quantity = 1
            }
            addedProductLineItemAdapter.submitList(checkedItems.toMutableList())
            qty = checkedItems.sumBy { it.quantity }
            totalPrice = checkedItems.sumByDouble { it.price * it.quantity.toDouble() }

            binding.textView4.text = totalPrice.toString()
            binding.textView3.text = qty.toString()
        }

        binding.sendOrderButton.setOnClickListener {
            if (addedProductLineItemAdapter.currentList.isEmpty()) {
                Toast.makeText(requireContext(), "Please add products to the order!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.recipientName.text.isEmpty() || binding.recipientsAddress.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in the recipient's name and address!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            viewModel.sendOrder(
                addedProductLineItemAdapter.currentList,
                totalPrice,
                binding.recipientsAddress.text.toString(),
                binding.recipientName.text.toString(),
                generateDate()
            )
            for (product in addedProductLineItemAdapter.currentList) {
                viewModel.updateQty(product, product.quantity)
            }
            Toast.makeText(requireContext(), "Order sent!", Toast.LENGTH_SHORT).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance())
                .commit()
        }

        binding.productsToAdd.apply {
            adapter = productLineItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addedProducts.apply {
            adapter = addedProductLineItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun generateDate(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}"

    }
}