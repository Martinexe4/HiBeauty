package com.capstone.hibeauty.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.RegisterRequest
import com.capstone.hibeauty.api.RegisterResponse
import com.capstone.hibeauty.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        supportActionBar?.hide()
        setContentView(binding.root)

        binding.registerSubmit.setOnClickListener {
            val username = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            val confirmPassword = binding.registerPasswordConfirm.text.toString()

            if (password == confirmPassword) {
                registerUser(username, email, password)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginButton.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        val request = RegisterRequest(username, email, password, password)
        val call = ApiConfig.apiService.registerUser(request)

        showLoading(true)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.registerProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.registerSubmit.isEnabled = !isLoading
        binding.registerName.isEnabled = !isLoading
        binding.registerEmail.isEnabled = !isLoading
        binding.registerPassword.isEnabled = !isLoading
        binding.registerPasswordConfirm.isEnabled = !isLoading
    }
}
