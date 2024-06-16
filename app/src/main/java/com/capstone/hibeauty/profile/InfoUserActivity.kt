package com.capstone.hibeauty.profile

import android.os.Bundle
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

        val userId = SharedPreferenceUtil.getUserId(this)
        if (userId != null) {
            fetchUserProfile(userId)
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }


        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun fetchUserProfile(userId: String) {
        val call = ApiConfig.apiService.getUserProfile2(userId)
        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val userProfile = response.body()?.data
                    binding.tvName.text = userProfile?.USERNAME
                    binding.tvAge.text = userProfile?.AGE.toString()
                    binding.tvGender.text = userProfile?.GENDER
                } else {
                    Toast.makeText(this@InfoUserActivity, "Failed to retrieve user profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(this@InfoUserActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}