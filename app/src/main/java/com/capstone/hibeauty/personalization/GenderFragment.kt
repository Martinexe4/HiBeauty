package com.capstone.hibeauty.personalization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.capstone.hibeauty.R
import com.capstone.hibeauty.databinding.FragmentGenderBinding

class GenderFragment : Fragment() {

    private var _binding: FragmentGenderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenderBinding.inflate(inflater, container, false)
        binding.buttonFinish.setOnClickListener {
            (activity as PersonalizationActivity).finishPersonalization()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getGender(): String {
        val selectedId = binding.radioGroupGender.checkedRadioButtonId
        val radioButton = view?.findViewById<RadioButton>(selectedId)
        return radioButton?.text.toString()
    }
}