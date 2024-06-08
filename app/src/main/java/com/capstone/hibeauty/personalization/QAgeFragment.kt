package com.capstone.hibeauty.personalization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.hibeauty.databinding.FragmentQAgeBinding

class QAgeFragment : Fragment() {

    private var _binding: FragmentQAgeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQAgeBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getAge(): String {
        return binding.etAge.text.toString()
    }

    fun isValid(): Boolean {
        return getAge().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}