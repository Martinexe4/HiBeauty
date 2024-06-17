package com.capstone.hibeauty.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.Product
import com.capstone.hibeauty.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)

        supportActionBar?.hide()
        setContentView(binding.root)

        val product = intent.getParcelableExtra<Product>("product")

        product?.let {
            with(binding) {
                productTitle.text = it.name
                productType.text = mapTypeIdToString(it.typeId)
                productDescription.text = it.description
                productIngredients.text = it.ingridients

                // Load image using Glide
                Glide.with(this@ProductDetailActivity)
                    .load(it.image) // Load the product image URL
                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                    .into(productImage)
            }
        }

        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.goToShopButton.setOnClickListener {
            product?.let {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                startActivity(browserIntent)
            }
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