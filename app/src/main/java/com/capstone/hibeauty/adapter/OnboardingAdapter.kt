package com.capstone.hibeauty.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.hibeauty.R
import com.capstone.hibeauty.welcome.OnboardingFragment

class OnboardingAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3  // Jumlah halaman onboarding
    private val context: Context = fragmentActivity

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.newInstance(
                context.getString(R.string.onboarding_title_1),
                context.getString(R.string.onboarding_desc_1),
                R.drawable.onboard_first  // Gambar wanita
            )
            1 -> OnboardingFragment.newInstance(
                context.getString(R.string.onboarding_title_2),
                context.getString(R.string.onboarding_desc_2),
                R.drawable.onboard_second  // Gambar pemindaian wajah
            )
            else -> OnboardingFragment.newInstance(
                context.getString(R.string.onboarding_title_3),
                context.getString(R.string.onboarding_desc_3),
                R.drawable.onboard_third  // Gambar skincare wajah
            )
        }
    }
}
