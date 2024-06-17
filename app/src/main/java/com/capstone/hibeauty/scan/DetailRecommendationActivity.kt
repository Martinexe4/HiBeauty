package com.capstone.hibeauty.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R

class DetailRecommendationActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var ingredientsTextView: TextView
    private lateinit var linkButton: Button
    private lateinit var closeButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_recommendation)

        // Inisialisasi view
        nameTextView = findViewById(R.id.nameTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        ingredientsTextView = findViewById(R.id.ingredientsTextView)
        linkButton = findViewById(R.id.linkButton)
        closeButton = findViewById(R.id.closeButton)

        // Ambil data dari intent
        val recommendationName = intent.getStringExtra("recommendationName")
        val recommendationDescription = intent.getStringExtra("recommendationDescription")
        val recommendationIngredients = intent.getStringExtra("recommendationIngredients")
        val recommendationLink = intent.getStringExtra("recommendationLink")

        // Set data ke view
        nameTextView.text = recommendationName
        descriptionTextView.text = recommendationDescription
        ingredientsTextView.text = "Ingredients: $recommendationIngredients"
        linkButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(recommendationLink)
            }
            startActivity(intent)
        }

        // Set close button action
        closeButton.setOnClickListener {
            finish()
        }
    }
}