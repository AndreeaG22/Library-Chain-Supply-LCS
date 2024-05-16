package com.teamapp.send_order

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.teamapp.home.HomeFragment
import com.teamapp.lcs.R
import com.teamapp.lcs.databinding.FragmentSendOrderBinding

class SendOrderFragment : Fragment() {

    private var _binding: FragmentSendOrderBinding? = null
    private val binding get() = _binding!!


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
    }

    private fun initUI() {
        binding.sendOrderButton.setOnClickListener {
            // Send order
            Toast.makeText(requireContext(), "Order sent!", Toast.LENGTH_SHORT).show()
            // go back to previous fragment go back to previous fragment
            parentFragmentManager.popBackStack() // TODO: check if this works
        }
    }
}