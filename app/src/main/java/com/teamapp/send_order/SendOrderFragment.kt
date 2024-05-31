package com.teamapp.send_order

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.teamapp.home.HomeFragment
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentSendOrderBinding
import java.io.File
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
            val addedProducts = addedProductLineItemAdapter.currentList.toMutableList()
            val checkedItems = productLineItemAdapter.currentList.filter { it.isChecked }

            // Add new checked items with quantity set to 1
            checkedItems.forEach { checkedItem ->
                if (!addedProducts.contains(checkedItem)) {
                    checkedItem.quantity = 1
                    addedProducts.add(checkedItem)
                }
            }

            // Remove unchecked items from addedProducts
            val uncheckedItems = productLineItemAdapter.currentList.filter { !it.isChecked }
            uncheckedItems.forEach { uncheckedItem ->
                addedProducts.remove(uncheckedItem)
            }

            addedProductLineItemAdapter.submitList(addedProducts)

            qty = addedProducts.sumBy { it.quantity }
            totalPrice = addedProducts.sumByDouble { it.price * it.quantity.toDouble() }

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

            savePdf(
                addedProductLineItemAdapter.currentList,
                totalPrice,
                binding.recipientsAddress.text.toString(),
                binding.recipientName.text.toString(),
                generateDate()
            )

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

    private fun savePdf(
        currentList: List<Product>,
        totalPrice: Double,
        toString: String,
        toString1: String,
        generateDate: String
    ) {
        // Save the order as a PDF
        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(700, 600, 1).create()

        val page = pdf.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint()
        paint.textSize = 20f
        var text = "Order"
        canvas.drawText(text, 80f, 50f, paint)

        paint.textSize = 10f
        text = "Date: $generateDate"
        canvas.drawText(text, 80f, 80f, paint)

        text = "Recipient: $toString1"
        canvas.drawText(text, 80f, 110f, paint)

        text = "Address: $toString"
        canvas.drawText(text, 80f, 140f, paint)

        text = "Products:"
        canvas.drawText(text, 80f, 170f, paint)

        var y = 200f
        currentList.forEach {
            text = "${it.name} - ${it.quantity} x ${it.price}"
            canvas.drawText(text, 80f, y, paint)
            y += 30f
        }

        text = "Total price: $totalPrice"
        canvas.drawText(text, 80f, y, paint)

        pdf.finishPage(page)

        val docsFolder = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val pdfFile = File(docsFolder, "order_$toString1-$generateDate.pdf")

        pdf.writeTo(pdfFile.outputStream())
        pdf.close()

        savePdfToFirebase(pdfFile)
    }

    private fun savePdfToFirebase(pdfFile: File) {
        // Save the PDF to Firebase
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val pdfRef = storageRef.child(pdfFile.name)

        val uri = Uri.fromFile(pdfFile)

        val uploadTask = pdfRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            pdfRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                // Save the download URL to the database
            }
        }.addOnFailureListener { exception ->
            println("Failed to upload PDF: ${exception.message}")
        }
    }


    private fun generateDate(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
            calendar.get(
                Calendar.YEAR
            )
        }"

    }
}