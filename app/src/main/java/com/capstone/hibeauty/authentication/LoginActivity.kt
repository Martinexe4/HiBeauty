package com.capstone.hibeauty.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.MainActivity
import com.capstone.hibeauty.databinding.ActivityLoginBinding
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

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            navigateToMainActivity()
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
                    val token = response.body()?.token // Ambil token dari response body
                    if (!token.isNullOrEmpty()) {
                        saveToken(token) // Simpan token ke Shared Preferences
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        saveLoginStatus(true)
                        navigateToMainActivity()
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Token is empty or null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token: String) {
        // Simpan token menggunakan SharedPreferenceUtil
        SharedPreferenceUtil.saveToken(this, token)
    }

    private fun getToken(): String? {
        // Ambil token menggunakan SharedPreferenceUtil
        return SharedPreferenceUtil.getToken(this)
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

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginSubmit.isEnabled = !isLoading
        binding.loginEmail.isEnabled = !isLoading
        binding.loginPassword.isEnabled = !isLoading
    }
}