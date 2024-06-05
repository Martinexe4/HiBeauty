package com.redy.cpv2.authentication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.redy.cpv2.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Tombol kembali khusus
        binding.customBackButton.setOnClickListener {
            finish() // Menutup aktivitas saat tombol kembali diklik
        }

        // Tombol untuk mengirim tautan reset
        binding.forgotSubmit.setOnClickListener {
            val email = binding.forgotEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                isEmailRegistered(email) { isRegistered ->
                    if (isRegistered) {
                        sendPasswordResetEmail(email)
                    } else {
                        Toast.makeText(this, "Email is not registered", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun isEmailRegistered(email: String, callback: (Boolean) -> Unit) {
        // Mencoba login dengan kata sandi acak untuk memeriksa apakah email sudah terdaftar
        val randomPassword = "randomPassword123"
        firebaseAuth.signInWithEmailAndPassword(email, randomPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuth.signOut() // Keluar karena kata sandi tidak valid
                    callback(true) // Email terdaftar
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        callback(false) // Email tidak terdaftar
                    } else {
                        Toast.makeText(this, "Error during email check", Toast.LENGTH_SHORT).show()
                        callback(false)
                    }
                }
            }
    }

    private fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showResetEmailSentDialog(email)
                } else {
                    Toast.makeText(this, "Error sending reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showResetEmailSentDialog(email: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Password Reset Sent")
            .setMessage("A reset password link has been sent to $email.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish() // Menutup aktivitas setelah dialog
            }
            .create()

        alertDialog.show()
    }
}