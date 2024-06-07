package com.redy.cpv2.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.redy.cpv2.R
import com.redy.cpv2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var currentPage = 0

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = firebaseAuth.currentUser
        loadUserName()

        binding.btnReadNow1.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_productFragment)
        )
        binding.btnReadNow2.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_homeFragment_to_cameraActivity)
        }

        val slides = listOf(
            mapOf(
                "title" to "Cuci Wajah dengan Lembut",
                "description" to "Gunakan pembersih wajah yang sesuai dengan jenis kulit Anda dan hindari menggosok terlalu keras.",
                "image" to R.drawable.slide_home_image1 // Ganti dengan ID gambar yang sesuai
            ),
            mapOf(
                "title" to "Gunakan Tabir Surya",
                "description" to "Selalu gunakan tabir surya dengan SPF minimal 30 setiap hari untuk melindungi kulit dari sinar UV.",
                "image" to R.drawable.slide_home_image2 // Ganti dengan ID gambar yang sesuai
            ),
            mapOf(
                "title" to "Pakai Pelembap",
                "description" to "Gunakan pelembap setiap hari untuk menjaga kelembapan kulit, pilih yang sesuai dengan jenis kulit Anda.",
                "image" to R.drawable.slide_home_image3 // Ganti dengan ID gambar yang sesuai
            )
        )

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = SlideInfoAdapter(slides)

        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Tab indicator customization if needed
        }.attach()

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (viewPager.adapter != null) {
                    currentPage = (currentPage + 1) % (viewPager.adapter!!.itemCount)
                    viewPager.setCurrentItem(currentPage, true)
                }
                handler.postDelayed(this, 5000)
            }
        }
        handler.postDelayed(runnable, 5000)
    }

    private fun loadUserName() {
        currentUser?.let { user ->
            val displayName = user.displayName ?: "User"
            binding.tvGreeting.text = "Hello $displayName,"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}