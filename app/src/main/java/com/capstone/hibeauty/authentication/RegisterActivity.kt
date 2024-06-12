package com.capstone.hibeauty.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerSubmit.setOnClickListener {
            val username = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            val confirmPassword = binding.registerPasswordConfirm.text.toString()

            // Validate input fields before sending the request
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoading(true)

            val registerRequest = RegisterRequest(username, email, password, confirmPassword)
            RetrofitInstance.api.register(registerRequest).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Register successful", Toast.LENGTH_SHORT).show()
                        // Start LoginActivity
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // Finish the current activity to remove it from the back stack
                    } else {
                        Toast.makeText(this@RegisterActivity, "Register failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity, "Register failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
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