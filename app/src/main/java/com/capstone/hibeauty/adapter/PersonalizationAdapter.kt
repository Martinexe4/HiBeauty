package com.capstone.hibeauty.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.hibeauty.personalization.QAgeFragment
import com.capstone.hibeauty.personalization.QGenderFragment
import com.capstone.hibeauty.personalization.QNameFragment

class PersonalizationAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        QNameFragment(),
        QAgeFragment(),
        QGenderFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getFragmentAt(position: Int): Fragment = fragments[position]
}
