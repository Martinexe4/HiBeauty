package com.redy.cpv2.product

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redy.cpv2.MainActivity
import com.redy.cpv2.R
import com.redy.cpv2.article.ApiConfigNews
import com.redy.cpv2.article.ApiServiceNews
import com.redy.cpv2.article.ArticleActivity
import com.redy.cpv2.article.ArticleAdapter
import com.redy.cpv2.databinding.ActivityArticleBinding
import com.redy.cpv2.databinding.ActivityProductBinding
import com.redy.cpv2.profile.ProfileActivity
import com.redy.cpv2.scan.ScanActivity

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