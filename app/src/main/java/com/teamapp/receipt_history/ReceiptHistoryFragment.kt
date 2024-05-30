package com.teamapp.receipt_history

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.teamapp.ReceiptData
import com.teamapp.lcs.databinding.FragmentReceiptHistoryBinding
import java.io.File

class ReceiptHistoryFragment : Fragment() {
    private var _binding: FragmentReceiptHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var receiptListAdapter: ReceiptLineItemAdapter

    companion object {
        fun newInstance() = ReceiptHistoryFragment()
    }

    private val viewModel: ReceiptHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel.getReceipts()
        viewModel.receipts.observe(viewLifecycleOwner) { receipts ->
            receiptListAdapter.submitList(receipts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        receiptListAdapter = ReceiptLineItemAdapter { item ->
            // Handle item deletion if needed
            downloadPdf(item)
        }
        binding.receiptList.apply {
            adapter = receiptListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun downloadPdf(item: ReceiptData) {
        val name = item.email
        val date = item.date

        // Download PDF file
        val storageRef = Firebase.storage.reference

        val orderName = "order_$name-$date.pdf"

        // Create the file in the external files directory
        val externalFilesDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (externalFilesDir != null) {
            val localFile = File(externalFilesDir, orderName)

            storageRef.child(orderName).getFile(localFile).addOnSuccessListener {
                // Local file has been created
                Toast.makeText(
                    requireContext(),
                    "File downloaded to ${localFile.absolutePath}",
                    Toast.LENGTH_SHORT
                ).show()
                // Optionally open the PDF file
                openPdf(localFile)
            }.addOnFailureListener {
                // Handle any errors
                Toast.makeText(requireContext(), "Error downloading file", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Error accessing external files directory",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPdf(localFile: File) {

        val pdfUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            localFile
        )

        val pdfIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(pdfUri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        val viewer = Intent.createChooser(pdfIntent, "Open PDF")
        startActivity(viewer)

    }


}