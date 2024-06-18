package com.capstone.hibeauty.profile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.UserProfileResponse
import com.capstone.hibeauty.databinding.ActivityInfoUserBinding
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val userId = SharedPreferenceUtil.getUserId(this) ?: ""
        val token = SharedPreferenceUtil.getToken(this) ?: ""
        fetchUserProfile(userId, token)

        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun fetchUserProfile(userId: String, token: String) {
        val call = ApiConfig.apiService.getUserProfile("Bearer $token", userId)

        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val userProfile = response.body()?.data
                    userProfile?.let {
                        binding.tvName.text = it.USERNAME
                        binding.tvAge.text = it.AGE.toString()
                        binding.tvGender.text = it.GENDER
                    }
                } else {
                    Log.e("ProfileInfo", "Failed to retrieve profile: ${response.errorBody()?.string()}")
                    Toast.makeText(this@InfoUserActivity, "Failed to retrieve profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e("ProfileInfo", "Error retrieving profile", t)
                Toast.makeText(this@InfoUserActivity, "Error retrieving profile", Toast.LENGTH_SHORT).show()
            }
        })
    }
}