package com.teamapp.resources_management

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamapp.lcs.databinding.FragmentResourcesManagementBinding
import com.teamapp.lcs.databinding.OrderProductLayoutBinding

class ResourcesManagementFragment : Fragment() {

    private var _binding: FragmentResourcesManagementBinding? = null
    private val binding get() = _binding!!
    private lateinit var resListAdapter: ResourcesLineItemAdapter

    companion object {
        fun newInstance() = ResourcesManagementFragment()
    }

    private val viewModel: ResourcesManagementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourcesManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        val resList = listOf(
            Product("Resource 1", 10),
            Product("Resource 2", 20),
            Product("Resource 3", 30),
            Product("Resource 4", 40),
            Product("Resource 5", 50)
        )
        resListAdapter.submitList(resList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        resListAdapter = ResourcesLineItemAdapter { item ->
            showOrderDialog(item)
        }
        binding.resList.apply {
            adapter = resListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showOrderDialog(item: Product) {
        // add order_product_layout.xml
        Dialog(binding.root.context).apply {

            val dialogBinding =
                OrderProductLayoutBinding.inflate(LayoutInflater.from(binding.root.context))
            setContentView(dialogBinding.root)
            dialogBinding.orderTitle.text = "Comanda Produs"
            dialogBinding.closeButton.text = "Inchide"
            dialogBinding.sendResourceOrderButton.text = "Trimite comanda"
            dialogBinding.orderTextView.text = "Comanda produsul ${item.name}"

            var qty = dialogBinding.quantityEditText.text.toString().toInt()

            dialogBinding.quantityEditText.doAfterTextChanged {
                if (dialogBinding.quantityEditText.text.toString().isNotEmpty()) {
                    qty = dialogBinding.quantityEditText.text.toString().toInt()
                }
            }

            dialogBinding.minusButton.setOnClickListener {
                if (qty > 0) {
                    qty--
                    dialogBinding.quantityEditText.setText((qty).toString())
                }
            }
            dialogBinding.plusButton.setOnClickListener {
                qty++
                dialogBinding.quantityEditText.setText((qty).toString())
            }

            dialogBinding.closeButton.setOnClickListener {
                dismiss()
            }

            dialogBinding.sendResourceOrderButton.setOnClickListener {
                Toast.makeText(context, "Comanda trimisa pentru ${qty} bucati", Toast.LENGTH_SHORT)
                    .show()
                dismiss()
            }
            show()
        }
    }

}