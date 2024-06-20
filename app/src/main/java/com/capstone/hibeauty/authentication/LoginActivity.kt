package com.capstone.hibeauty.authentication

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.MainActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.LoginRequest
import com.capstone.hibeauty.api.LoginResponse
import com.capstone.hibeauty.api.UserProfileResponse
import com.capstone.hibeauty.databinding.ActivityLoginBinding
import com.capstone.hibeauty.personalization.PersonalizationActivity
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val sharedPref by lazy { getSharedPreferences("HiBeautyPrefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        supportActionBar?.hide()
        setContentView(binding.root)

        if (isUserLoggedIn()) {
            handleLoginSuccess()
            finish()
        }

        binding.loginSubmit.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            loginUser(email, password)
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        val request = LoginRequest(email, password)
        val call = ApiConfig.apiService.loginUser(request)

        showLoading(true)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (!token.isNullOrEmpty()) {
                        saveToken(token)
                        Toast.makeText(this@LoginActivity, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                        saveLoginStatus(true)
                        handleLoginSuccess()
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, getString(R.string.token_empty_or_null), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token: String) {
        SharedPreferenceUtil.saveToken(this, token)
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        with(sharedPref.edit()) {
            putBoolean("IS_LOGGED_IN", isLoggedIn)
            apply()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPref.getBoolean("IS_LOGGED_IN", false)
    }

    private fun handleLoginSuccess() {
        val token = SharedPreferenceUtil.getToken(this)
        if (token != null) {
            fetchUserProfile(token)
        } else {
            Toast.makeText(this, getString(R.string.error_token_not_found), Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserProfile(token: String) {
        val userId = SharedPreferenceUtil.getUserId(this)

        val call = userId?.let { ApiConfig.apiService.getUserProfile("Bearer $token", it) }
        call?.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()?.data
                    if (userProfile != null && userProfile.AGE != 0 && userProfile.GENDER.isNotEmpty()) {
                        navigateToMainActivity()
                    } else {
                        navigateToPersonalizationActivity()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, getString(R.string.failed_to_fetch_user_profile), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, getString(R.string.error_fetching_user_profile), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        val mBundle: Bundle = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity).toBundle()
        startActivity(intent, mBundle)
        finish()
    }

    private fun navigateToPersonalizationActivity() {
        val intent = Intent(this, PersonalizationActivity::class.java)
        val mBundle: Bundle = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity).toBundle()
        startActivity(intent, mBundle)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginSubmit.isEnabled = !isLoading
        binding.loginEmail.isEnabled = !isLoading
        binding.loginPassword.isEnabled = !isLoading
    }
}
