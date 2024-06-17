package com.capstone.hibeauty.ui.profile

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.ApiService
import com.capstone.hibeauty.api.ProfileImageResponse
import com.capstone.hibeauty.api.UserProfileResponse
import com.capstone.hibeauty.authentication.LoginActivity
import com.capstone.hibeauty.databinding.FragmentProfileBinding
import com.capstone.hibeauty.profile.InfoUserActivity
import com.capstone.hibeauty.profile.LanguageActivity
import com.capstone.hibeauty.profile.PolicyActivity
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File
import java.io.IOException


class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var userService: ApiService // Ensure this is initialized properly
    private lateinit var profileImageView: ImageView



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
        profileImageView = view.findViewById(R.id.imgProfile) // Ensure you have an ImageView with this ID in your layout

        loadUserImage()
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
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            uploadImage()
        }
    }

    //maaf sangat berantakan di sini
    private fun uploadImage() {
        val userId = SharedPreferenceUtil.getUserId(requireActivity())
        val token = SharedPreferenceUtil.getToken(requireActivity())

        val contentResolver = requireActivity().contentResolver
        val inputStream = contentResolver.openInputStream(selectedImageUri!!)
        val file = File(requireActivity().cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = file.outputStream()
        inputStream!!.copyTo(outputStream)

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("IMAGE", file.name, requestFile)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-q4bx5v5sia-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val call = userId?.let { service.uploadProfileImage("Bearer $token", it, body) }

        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle success
                    Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    loadUserImage()

                } else {
                    // Handle failure
                    Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle failure
                Toast.makeText(requireContext(), "Image upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Extension function to get the file name from the Uri
    private fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val returnCursor = this.query(uri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
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

    private fun loadUserImage() {
        val apiService = ApiConfig.apiService
        val token = SharedPreferenceUtil.getToken(requireContext())
        val userId = SharedPreferenceUtil.getUserId(requireContext())

        Log.d("ProfileFragment", "Token: $token, UserId: $userId")

        if (token != null && userId != null) {
            val call = apiService.getUserProfileImage("Bearer $token", userId)

            call.enqueue(object : Callback<ProfileImageResponse> {
                override fun onResponse(call: Call<ProfileImageResponse>, response: Response<ProfileImageResponse>) {
                    if (response.isSuccessful) {
                        val profileImage = response.body()?.data?.profileImage
                        if (profileImage != null) {
                            Glide.with(this@ProfileFragment)
                                .load(profileImage)
                                .placeholder(R.drawable.placeholder_image) // Add a placeholder image in your drawable resources
                                .into(profileImageView)
                        } else {
                            profileImageView.setImageResource(R.drawable.placeholder_image)
                        }
                    } else {
                        Log.d("ProfileFragment", "Response not successful")
                        profileImageView.setImageResource(R.drawable.placeholder_image)
                    }
                }

                override fun onFailure(call: Call<ProfileImageResponse>, t: Throwable) {
                    Log.d("ProfileFragment", "Failed to fetch user profile image: ${t.message}")
                    profileImageView.setImageResource(R.drawable.placeholder_image)
                }
            })
        } else {
            Log.d("ProfileFragment", "Token or UserId not found")
            profileImageView.setImageResource(R.drawable.placeholder_image)
        }
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