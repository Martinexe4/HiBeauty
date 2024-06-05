package com.redy.cpv2.welcome



import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redy.cpv2.R

class OnboardingAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3  // Jumlah halaman onboarding

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.newInstance(
                "Selamat Datang",
                "Temukan produk skincare yang tepat untuk kulit Anda. Kami membantu Anda merawat wajah dengan rekomendasi yang personal.",
                R.drawable.onboard_first  // Gambar wanita
            )
            1 -> OnboardingFragment.newInstance(
                "Pindai Wajah Anda",
                "Gunakan fitur pemindaian untuk menganalisis kondisi kulit Anda. Ini adalah langkah pertama untuk rekomendasi skincare yang tepat.",
                R.drawable.onboard_second  // Gambar pemindaian wajah
            )
            else -> OnboardingFragment.newInstance(
                "Rekomendasi Skincare",
                "Berdasarkan hasil pemindaian, kami akan merekomendasikan produk skincare yang paling sesuai untuk Anda. Siap untuk memulai perjalanan perawatan kulit Anda?",
                R.drawable.onboard_third  // Gambar skincare wajah
            )
        }
    }
}
