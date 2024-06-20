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
        setContentView(binding.root)
        supportActionBar?.hide()

        val product = intent.getParcelableExtra<Product>("product")

        product?.let {
            with(binding) {
                productTitle.text = it.name
                productType.text = mapTypeIdToString(it.typeId)
                productDescription.text = it.description
                productIngredients.text = it.ingridients

                Glide.with(this@ProductDetailActivity)
                    .load(it.image)
                    .placeholder(R.drawable.placeholder_image)
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
            1 -> getString(R.string.moisturizer)
            2 -> getString(R.string.cleanser)
            3 -> getString(R.string.powder)
            4 -> getString(R.string.balm)
            5 -> getString(R.string.serum)
            6 -> getString(R.string.toner)
            7 -> getString(R.string.face_wash)
            8 -> getString(R.string.eye_cream)
            9 -> getString(R.string.face_scrub)
            10 -> getString(R.string.sunscreen)
            11 -> getString(R.string.micellar_water)
            12 -> getString(R.string.acne_spot)
            else -> getString(R.string.unknown)
        }
    }
}