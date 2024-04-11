package com.example.homesecurity.ui.smartobj

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.homesecurity.databinding.FragmentSmartobjBinding

class SmartobjFragment : Fragment() {

    private var _binding: FragmentSmartobjBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmartobjBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val addSOButton: ImageButton = binding.btnAddSmartObject;


        addSOButton.setOnClickListener {
            Toast.makeText(context, "button addSmartObject pressed", Toast.LENGTH_LONG).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}