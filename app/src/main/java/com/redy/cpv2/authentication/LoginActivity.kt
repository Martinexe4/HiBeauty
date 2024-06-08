package com.redy.cpv2.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.redy.cpv2.MainActivity
import com.redy.cpv2.R
import com.redy.cpv2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Mengakhiri LoginActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Jika pengguna sudah masuk, langsung ke MainActivity
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Mengakhiri LoginActivity
            return
        }

        // Atur Google Sign-In Client
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Event handler untuk Google Sign-In
        binding.googleSignInButton.setOnClickListener {
            showLoading(true)  // Menampilkan loading
            signIn()  // Memulai proses Google Sign-In
        }

        // Event handler untuk Login Button
        binding.loginSubmit.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                showLoading(true)  // Indikator loading
                loginProcess(email, password)  // Memulai proses login
            } else {
                Toast.makeText(this, "Data login cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Event handler untuk Register Button
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun loginProcess(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()  // Mengakhiri LoginActivity
            }
            .addOnFailureListener { error ->
                showLoading(false)  // Menyembunyikan loading
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                showLoading(false)  // Menyembunyikan loading setelah operasi selesai
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)  // Melanjutkan autentikasi Google
                } else {
                    showLoading(false)  // Menyembunyikan loading jika gagal
                    Toast.makeText(this, "Sign-in Failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                showLoading(false)  // Menyembunyikan loading jika terjadi kesalahan
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()  // Mengakhiri LoginActivity
            }
            .addOnFailureListener { error ->
                showLoading(false)  // Menyembunyikan loading
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                showLoading(false)  // Menyembunyikan loading setelah operasi selesai
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
