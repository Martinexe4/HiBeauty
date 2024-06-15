package com.capstone.hibeauty.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.hibeauty.R
import com.capstone.hibeauty.authentication.LoginActivity
import com.capstone.hibeauty.databinding.FragmentProfileBinding
import com.capstone.hibeauty.authentication.ApiConfig
import com.capstone.hibeauty.profile.HistoryActivity
import com.capstone.hibeauty.profile.InfoUserActivity
import com.capstone.hibeauty.profile.LanguageActivity
import com.capstone.hibeauty.profile.PolicyActivity
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import com.capstone.hibeauty.authentication.User
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    var binding: FragmentProfileBinding? = null
    private val PICK_IMAGE_REQUEST = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUserProfile()

        binding?.iconEditPhoto?.setOnClickListener {
            choosePhoto()
        }

        binding?.logOut?.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding?.privacyPolicy?.setOnClickListener {
            val intent = Intent(activity, PolicyActivity::class.java)
            startActivity(intent)
        }

        binding?.infoUser?.setOnClickListener {
            val intent = Intent(activity, InfoUserActivity::class.java)
            startActivity(intent)
        }

        binding?.historyAnalysis?.setOnClickListener {
            val intent = Intent(activity, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding?.changeLanguage?.setOnClickListener {
            val intent = Intent(activity, LanguageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun choosePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }



    private fun fetchUserProfile() {
        val apiService = ApiConfig.apiService
        val token = SharedPreferenceUtil.getToken(requireContext())

        Log.d("ProfileFragment", "Token: $token")

        if (token != null) {
            val call = apiService.getUserProfile("Bearer $token")

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()

                        Log.d("ProfileFragment", "Response body: ${response.body()}")
                        user?.let {
                            Log.d("ProfileFragment", "Username: ${it.USERNAME}")
                            binding?.txtDisplayName?.text = it.USERNAME

                            if (it.PROFILEIMG != null) {
                                // Load image using Picasso if PROFILEIMG is not null
                                Picasso.get().load(it.PROFILEIMG).into(binding?.imgProfile)
                            } else {
                                // Handle case where PROFILEIMG is null (e.g., show a placeholder image)
                                // Example:
                                // Picasso.get().load(R.drawable.placeholder_image).into(binding?.imgProfile)
                                // or
                                 binding?.imgProfile?.setImageResource(R.drawable.placeholder_image)
                            }
                        }
                    } else {
                        Log.d("ProfileFragment", "Failed to fetch user profile: ${response.code()}")
                        showToast("Failed to fetch user profile: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("ProfileFragment", "Failed to fetch user profile: ${t.message}")
                    showToast("Failed to fetch user profile: ${t.message}")
                }
            })
        } else {
            Log.d("ProfileFragment", "Token not found")
            showToast("Token not found")
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                // Menghapus status login dari SharedPreference
                SharedPreferenceUtil.saveLoginStatus(requireActivity(), false)

                // Navigasi ke LoginActivity dan menutup semua activity sebelumnya
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}