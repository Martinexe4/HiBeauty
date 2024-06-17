package com.capstone.hibeauty.ui.profile

import android.content.Intent
import android.content.pm.PackageManager
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
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.UserProfileResponse
import com.capstone.hibeauty.authentication.LoginActivity
import com.capstone.hibeauty.databinding.FragmentProfileBinding
import com.capstone.hibeauty.profile.InfoUserActivity
import com.capstone.hibeauty.profile.LanguageActivity
import com.capstone.hibeauty.profile.PolicyActivity
import com.capstone.hibeauty.utils.SharedPreferenceUtil
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
        val userId = SharedPreferenceUtil.getUserId(requireContext())

        Log.d("ProfileFragment", "Token: $token, UserId: $userId")

        if (token != null && userId != null) {
            val call = apiService.getUserProfile("Bearer $token", userId)

            call.enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                    if (response.isSuccessful) {
                        val userProfileResponse = response.body()

                        if (userProfileResponse != null && userProfileResponse.status) {
                            val user = userProfileResponse.data
                            binding?.txtDisplayName?.text = user.USERNAME
                            // Load user profile picture if available
//                            if (!user.profilePicture.isNullOrEmpty()) {
//                                Picasso.get().load(user.profilePicture).into(binding?.profileImageView)
//                            }
                        } else {
                            Log.d("ProfileFragment", "Response body is null or status is false")
                        }
                    } else {
                        Log.d("ProfileFragment", "Failed to fetch user profile: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Log.d("ProfileFragment", "Failed to fetch user profile: ${t.message}")
                }
            })
        } else {
            Log.d("ProfileFragment", "Token or UserId not found")
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