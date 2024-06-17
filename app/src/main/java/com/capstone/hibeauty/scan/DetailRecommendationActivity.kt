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

        // Ambil data dari intent
        val recommendationName = intent.getStringExtra("recommendationName")
        val recommendationDescription = intent.getStringExtra("recommendationDescription")
        val recommendationIngredients = intent.getStringExtra("recommendationIngredients")
        val recommendationLink = intent.getStringExtra("recommendationLink")
        val recommendationImage = intent.getStringExtra("recommendationImage") // tambahkan ini

        // Set data ke view
        binding.productTitle.text = recommendationName
        binding.productDescription.text = recommendationDescription
        binding.productIngredients.text = recommendationIngredients

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
}