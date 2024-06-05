package com.redy.cpv2.scan

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.firebase.firestore.firestore
import com.redy.cpv2.R
import com.redy.cpv2.databinding.ActivityResultBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var imageUri: Uri? = null
    private var category: String? = null
    private var probability: Float? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = Firebase.firestore

        imageUri = intent.getStringExtra("imageUri")?.toUri()
        category = intent.getStringExtra("category")
        probability = intent.getFloatExtra("probability", 0f)

        displayResult()

        binding.saveResultButton.setOnClickListener {
            saveResultToFirestore()
        }
    }

    private fun displayResult() {
        imageUri?.let {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
            binding.resultImageView.setImageBitmap(bitmap)
        }
        binding.categoryTextView.text = category
        binding.probabilityTextView.text = getString(R.string.probability_format, probability)
    }

    private fun saveResultToFirestore() {
        if (imageUri != null && category != null && probability != null) {
            val resultData = hashMapOf(
                "imageUri" to imageUri.toString(),
                "category" to category!!,
                "probability" to probability!!
            )

            firestore.collection("results")
                .add(resultData)
                .addOnSuccessListener {
                    showToast("Result saved successfully")
                }
                .addOnFailureListener { e ->
                    showToast("Error saving result: ${e.message}")
                }
        } else {
            showToast("Incomplete data, cannot save result")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "ResultActivity"
    }
}