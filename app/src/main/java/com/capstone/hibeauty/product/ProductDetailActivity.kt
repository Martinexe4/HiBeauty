package com.capstone.hibeauty.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.adapter.ImageSliderAdapter
import com.capstone.hibeauty.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val product = intent.getParcelableExtra<Product>("product")

        product?.let {
            binding.productTitle.text = it.name
            binding.productDescription.text = it.description

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