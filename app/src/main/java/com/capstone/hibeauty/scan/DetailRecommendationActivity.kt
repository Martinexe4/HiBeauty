package com.capstone.hibeauty.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.hibeauty.R
import com.capstone.hibeauty.databinding.ActivityDetailRecommendationBinding

class DetailRecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRecommendationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Ambil data dari intent
        val recommendationName = intent.getStringExtra("recommendationName")
        val recommendationDescription = intent.getStringExtra("recommendationDescription")
        val recommendationIngredients = intent.getStringExtra("recommendationIngredients")
        val recommendationLink = intent.getStringExtra("recommendationLink")
        val recommendationImage = intent.getStringExtra("recommendationImage")// tambahkan ini
        val recommendationType = intent.getIntExtra("recommendationType", -1)

        // Set data ke view
        binding.productTitle.text = recommendationName
        binding.productDescription.text = recommendationDescription
        binding.productIngredients.text = recommendationIngredients
        binding.productType.text = mapTypeIdToString(recommendationType)

        // Load image using Glide
        Glide.with(this)
            .load(recommendationImage) // Load the product image URL
            .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
            .into(binding.productImage)

        // Set click listener untuk link button
        binding.goToShopButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(recommendationLink)
            }
            startActivity(intent)
        }

        // Set click listener untuk close button
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun mapTypeIdToString(typeId: Int): String {
        return when (typeId) {
            1 -> "Moisturizer"
            2 -> "Cleanser"
            3 -> "Powder"
            4 -> "Balm"
            5 -> "Serum"
            6 -> "Toner"
            7 -> "Face Wash"
            8 -> "Eye Cream"
            9 -> "Face Scrub"
            10 -> "Sunscreen"
            11 -> "Micellar Water"
            12 -> "Acne Spot"
            else -> "Unknown"
        }
    }
}