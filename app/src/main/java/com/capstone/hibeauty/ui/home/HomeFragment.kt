package com.capstone.hibeauty.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.capstone.hibeauty.MainActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.adapter.SlideInfoAdapter
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.UserProfileResponse
import com.capstone.hibeauty.databinding.FragmentHomeBinding
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NAME_SHADOWING")
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var currentPage = 0


    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_cameraActivity)
            } else {
                Toast.makeText(MainActivity(), getString(R.string.required_camera), Toast.LENGTH_SHORT).show()
            }
        }

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

        loadUserName()

        binding.btnReadNow1.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_homeFragment_to_productFragment)
        }
        binding.btnReadNow2.setOnClickListener {
            checkCameraPermission()
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

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_cameraActivity)
            }

            else -> {
                cameraPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun loadUserName() {
        val apiService = ApiConfig.apiService
        val token = SharedPreferenceUtil.getToken(requireContext())
        val userId = SharedPreferenceUtil.getUserId(requireContext())

        Log.d("HomeFragment", "Token $token, UserId $userId")

        if (token != null && userId != null) {
            val call = apiService.getUserProfile("Bearer $token", userId)

            call.enqueue(object : Callback<UserProfileResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                    if (response.isSuccessful) {
                        val userProfileResponse = response.body()

                        if (userProfileResponse != null && userProfileResponse.status) {
                            val user = userProfileResponse.data
                            binding.tvGreeting.text = "Hello ${user.USERNAME}"
                        } else {
                            Log.d("HomeFragment", "Response body is null or status is false")
                        }
                    } else {
                        Log.d("HomeFragment", "Failed to fetch user profile: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Log.d("HomeFragment", "Failed to fetch user profile: ${t.message}")
                }
            })
        } else {
            Log.d("HomeFragment", "Token or UserId not found")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}