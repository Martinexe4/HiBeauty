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

        binding.productTitle.text = recommendationName
        binding.productDescription.text = recommendationDescription
        binding.productIngredients.text = recommendationIngredients
        binding.productType.text = mapTypeIdToString(recommendationType)

        Glide.with(this)
            .load(recommendationImage)
            .placeholder(R.drawable.placeholder_image)
            .into(binding.productImage)

        binding.goToShopButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(recommendationLink)
            }
            startActivity(intent)
        }

        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun mapTypeIdToString(typeId: Int): String {
        return when (typeId) {
            1 -> getString(R.string.moisturizer)
            2 -> getString(R.string.cleanser)
            3 -> getString(R.string.powder)
            4 -> getString(R.string.balm)
            5 -> getString(R.string.serum)
            6 -> getString(R.string.toner)
            7 -> getString(R.string.face_wash)
            8 -> getString(R.string.face_scrub)
            9 -> getString(R.string.eye_cream)
            10 -> getString(R.string.cream)
            11 -> getString(R.string.sunscreen)
            12 -> getString(R.string.micellar_water)
            else -> getString(R.string.unknown)
        }
    }
}