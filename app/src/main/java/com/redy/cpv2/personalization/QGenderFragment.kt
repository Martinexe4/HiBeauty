package com.redy.cpv2.personalization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.redy.cpv2.R
import com.redy.cpv2.databinding.FragmentQGenderBinding

class QGenderFragment : Fragment() {

    private var _binding: FragmentQGenderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getGender(): String {
        return when (binding.rgGender.checkedRadioButtonId) {
            R.id.rb_male -> "Male"
            R.id.rb_female -> "Female"
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}