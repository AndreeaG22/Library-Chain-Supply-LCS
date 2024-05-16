package com.teamapp.employee_management

import android.app.Dialog
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.ErrorDialogBinding
import com.teamapp.lcs.databinding.FragmentAddEmployeeBinding
import java.util.Calendar

class AddEmployeeFragment : Fragment() {

    private var _binding: FragmentAddEmployeeBinding? = null
    private val binding get() = _binding!!


    private val viewModel: AddEmployeeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        val calendar = Calendar.getInstance()
        var email: String = ""
        var name: String = ""
        var code: String = ""
        var role: String = ""
        var phone: String = ""
        var date: String = ""

        // Initialize DatePicker with current date
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
        val initialDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
        binding.empDate.setText(initialDate)

        // Make the TextView non-clickable and non-focusable
        binding.empDate.isClickable = false
        binding.empDate.isFocusable = false


        binding.addEmployeeButton.setOnClickListener {
            if (binding.empEmail.text.isNotEmpty() && binding.empName.text.isNotEmpty() && binding.empCnp.text.isNotEmpty() && binding.empRole.text.isNotEmpty() && binding.empPhone.text.isNotEmpty()) {
                email = binding.empEmail.text.toString()
                name = binding.empName.text.toString()
                code = binding.empCnp.text.toString()
                role = binding.empRole.text.toString()
                phone = binding.empPhone.text.toString()
                date = binding.empDate.text.toString()
//                viewModel.addEmployee(email, name, code, role, phone, date)
                parentFragmentManager.popBackStack()
            } else {
                // Show error dialog if any field is empty
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