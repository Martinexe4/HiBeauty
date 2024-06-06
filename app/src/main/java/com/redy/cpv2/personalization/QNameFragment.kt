package com.redy.cpv2.personalization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.redy.cpv2.R
import com.redy.cpv2.databinding.FragmentQNameBinding

class QNameFragment : Fragment() {

    private var _binding: FragmentQNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getName(): String {
        return binding.etName.text.toString()
    }

    fun isValid(): Boolean {
        return getName().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}