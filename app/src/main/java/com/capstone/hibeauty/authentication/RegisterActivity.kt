package com.capstone.hibeauty.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.RegisterRequest
import com.capstone.hibeauty.api.RegisterResponse
import com.capstone.hibeauty.databinding.ActivityRegisterBinding
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val TAG = "RegisterActivity"

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
                Toast.makeText(this, R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show()
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
                    val registerResponse = response.body()
                    Log.d(TAG, "Response body: $registerResponse")
                    if (registerResponse?.status == true) {
                        Toast.makeText(this@RegisterActivity, registerResponse.message, Toast.LENGTH_SHORT).show()

                        val userid = registerResponse.data?.USERID
                        if (userid != null) {
                            Log.d(TAG, "UserID from response: $userid")
                            SharedPreferenceUtil.saveUserId(this@RegisterActivity, userid)

                            val savedUserid = SharedPreferenceUtil.getUserId(this@RegisterActivity)
                            Log.d(TAG, "UserID from SharedPreferences: $savedUserid")
                        }

                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val message = registerResponse?.message ?: getString(R.string.unknown_error)
                        Toast.makeText(this@RegisterActivity, getString(R.string.registration_failed_with_message, message), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: getString(R.string.unknown_error)
                    Log.d(TAG, "Response unsuccessful: $errorMessage")
                    Toast.makeText(this@RegisterActivity, getString(R.string.registration_failed_with_message, errorMessage), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@RegisterActivity, getString(R.string.registration_failed_with_message, t.message), Toast.LENGTH_SHORT).show()
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