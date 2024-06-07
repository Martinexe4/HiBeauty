package com.redy.cpv2.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.redy.cpv2.R
import com.redy.cpv2.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        val imageResId = arguments?.getInt("imageResId")

        binding.textTitle.text = title
        binding.textDescription.text = description
        if (imageResId != null) {
            Glide.with(this).load(imageResId).into(binding.imageIcon)
        }
    }

    companion object {
        fun newInstance(title: String, description: String, imageResId: Int): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("description", description)
            args.putInt("imageResId", imageResId)
            fragment.arguments = args
            return fragment
        }
    }
}