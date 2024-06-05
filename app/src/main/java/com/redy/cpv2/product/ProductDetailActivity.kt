package com.redy.cpv2.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redy.cpv2.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getParcelableExtra<Product>("product")

        product?.let {
            binding.productTitle.text = it.name
            binding.productPrice.text = it.price
            binding.productDescription.text = it.description
            binding.productRating.rating = it.rating

            val adapter = ImageSliderAdapter(this, it.imageList)
            binding.productImageSlider.adapter = adapter
        }

        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.goToShopButton.setOnClickListener {
            // Add logic to handle "Go to Shop" action
        }
    }
}