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
import com.teamapp.lcs.databinding.AddProductLayoutBinding
import com.teamapp.lcs.databinding.ErrorDialogBinding
import com.teamapp.lcs.databinding.FragmentResourcesManagementBinding
import com.teamapp.lcs.databinding.OrderProductLayoutBinding
import com.teamapp.send_order.Product

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
        observeViewModel()

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
        binding.addResource.setOnClickListener {
            Dialog(binding.root.context).apply {
                val dialogBinding =
                    AddProductLayoutBinding.inflate(LayoutInflater.from(binding.root.context))
                setContentView(dialogBinding.root)
                dialogBinding.addTitle.text = "Adauga Produs"
                dialogBinding.closeButton.text = "Inchide"

                dialogBinding.closeButton.setOnClickListener {
                    dismiss()
                }

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

                dialogBinding.addResourceButton.setOnClickListener {
                    val name = dialogBinding.productNameEditText.text.toString()
                    val qtyText = dialogBinding.quantityEditText.text.toString()
                    val priceText = dialogBinding.productPriceEditText.text.toString()

                    if (name.isNotEmpty()) {
                        val qty = if (qtyText.isNotEmpty()) {
                            qtyText.toIntOrNull() ?: 0
                        } else {
                            0
                        }

                        if (qty == 0) {
                            showErrorDialog("Cantitatea nu poate fi 0")
                            return@setOnClickListener
                        }

                        if (priceText.isNotEmpty()) {
                            val price = priceText.toDoubleOrNull()
                            if (price != null) {
                                Toast.makeText(
                                    context,
                                    "Produs adaugat: $name, $qty bucati, pret: $price",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.addResource(name, qty, price)
                                dismiss()
                            } else {
                                showErrorDialog("Pretul nu este valid")
                            }
                        } else {
                            showErrorDialog("Pretul nu poate fi gol")
                        }
                    } else {
                        showErrorDialog("Numele nu poate fi gol")
                    }
                }

                show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            resListAdapter.submitList(products)
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
                if (qty == 0) {
                    showErrorDialog("Cantitatea nu poate fi 0")
                    return@setOnClickListener
                } else {
                    Toast.makeText(
                        context,
                        "Comanda trimisa pentru ${qty} bucati",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    viewModel.supplyResource(item.name, qty)
                    dismiss()
                }
            }
            show()
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