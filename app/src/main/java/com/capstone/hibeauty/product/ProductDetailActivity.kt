package com.capstone.hibeauty.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.adapter.ImageSliderAdapter
import com.capstone.hibeauty.authentication.Product
import com.capstone.hibeauty.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getParcelableExtra<Product>("product")

        product?.let {
            with(binding) {
                productTitle.text = it.name
                productDescription.text = it.description
                productIngredients.text = it.ingridients
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
}