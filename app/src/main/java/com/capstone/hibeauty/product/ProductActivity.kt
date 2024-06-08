package com.capstone.hibeauty.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.MainActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.article.ArticleActivity
import com.capstone.hibeauty.databinding.ActivityProductBinding
import com.capstone.hibeauty.profile.ProfileActivity
import com.capstone.hibeauty.scan.ScanActivity

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding // Binding instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.product -> {
                    false
                }
                R.id.news -> {
                    val intent = Intent(this, ArticleActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        binding.floatButton.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }

    }
}