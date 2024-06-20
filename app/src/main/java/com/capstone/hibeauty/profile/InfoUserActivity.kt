package com.capstone.hibeauty.profile

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.UserProfileResponse
import com.capstone.hibeauty.databinding.ActivityInfoUserBinding
import com.capstone.hibeauty.utils.ContextWrapper
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoUserBinding
    private lateinit var preference: LanguagePreference

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

    override fun attachBaseContext(newBase: Context?) {
        preference = LanguagePreference(newBase!!)
        val lang = preference.loadSettingLanguage()
        super.attachBaseContext(ContextWrapper.wrap(newBase, lang.toString()))
    }

    private fun fetchUserProfile(userId: String, token: String) {
        val call = ApiConfig.apiService.getUserProfile("Bearer $token", userId)

        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val userProfile = response.body()?.data
                    var translatedGender = ""
                    userProfile?.let {
                        if (preference.loadSettingLanguage() == "in") {
                            when (it.GENDER) {
                                "Male" -> translatedGender = "Laki-Laki"
                                "Female" -> translatedGender = "Perempuan"
                                else -> translatedGender = it.GENDER
                            }
                        } else {
                            translatedGender = it.GENDER
                        }

                        binding.tvName.text = it.USERNAME
                        binding.tvAge.text = it.AGE.toString()
                        binding.tvGender.text = translatedGender
                    }
                } else {
                    Toast.makeText(this@InfoUserActivity, R.string.failed_to_retrieve_profile, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(this@InfoUserActivity, R.string.error_retrieving_profile, Toast.LENGTH_SHORT).show()
            }
        })
    }
}