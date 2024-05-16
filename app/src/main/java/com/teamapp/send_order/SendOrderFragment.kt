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
        val productList = listOf(
            Product("Caiet", 12.0),
            Product("Pix", 15.0),
            Product("Creion", 20.0),
            Product("Radiera", 25.0),
            Product("Creta", 30.0)
        )
        productLineItemAdapter.submitList(productList.toMutableList())
    }

    private fun initUI() {
        var qty = 0
        var totalPrice = 0.0
        binding.sendOrderButton.setOnClickListener {
            // Send order
            Toast.makeText(requireContext(), "Order sent!", Toast.LENGTH_SHORT).show()
            // go back to previous fragment go back to previous fragment
//            parentFragmentManager.popBackStack() // TODO: check if this works
            // clear back stack and go to home fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance())
                .commit()
        }

        addedProductLineItemAdapter = AddedProductLineItemAdapter { item ->
//            viewModel.removeProduct(item)
        }

        productLineItemAdapter = ProductLineItemAdapter {
            val crtList = productLineItemAdapter.currentList

            val checkedItems = crtList.filter { it.isChecked }
            addedProductLineItemAdapter.submitList(checkedItems.toMutableList())
            qty = checkedItems.size
            totalPrice = checkedItems.sumByDouble { it.price }

            binding.textView4.text = totalPrice.toString()
            binding.textView3.text = qty.toString()
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
}