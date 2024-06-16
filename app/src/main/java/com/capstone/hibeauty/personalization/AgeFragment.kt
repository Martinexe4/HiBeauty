package com.capstone.hibeauty.personalization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.hibeauty.databinding.FragmentAgeBinding


class AgeFragment : Fragment() {

    private var _binding: FragmentAgeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgeBinding.inflate(inflater, container, false)
        binding.buttonNext.setOnClickListener {
            (activity as PersonalizationActivity).goToNextPage()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getAge(): Int {
        val ageString = binding.editTextAge.text.toString()
        return if (ageString.isNotEmpty()) ageString.toInt() else 0
    }
}