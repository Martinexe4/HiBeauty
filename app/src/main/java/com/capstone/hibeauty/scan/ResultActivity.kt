package com.capstone.hibeauty.scan

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.capstone.hibeauty.databinding.ActivityResultBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var imageUri: Uri? = null
    private var results: Map<String, Float>? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        firestore = Firebase.firestore

        imageUri = intent.getStringExtra("imageUri")?.toUri()
        results = intent.getSerializableExtra("results") as? Map<String, Float>

        displayResult()

        binding.saveResultButton.setOnClickListener {
            saveResultToFirestore()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun displayResult() {
        imageUri?.let {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
            binding.resultImageView.setImageBitmap(bitmap)
        }

        results?.let {
            val resultsText = it.entries.joinToString("\n") { entry ->
                "${entry.key}: ${String.format("%.2f%%", entry.value * 100)}"
            }
            binding.resultTextView?.text = resultsText
        }
    }

    private fun saveResultToFirestore() {

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}