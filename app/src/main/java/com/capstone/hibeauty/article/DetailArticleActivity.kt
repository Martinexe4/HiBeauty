package com.capstone.hibeauty.article

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.hibeauty.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Terima data artikel dari Intent
        val article = intent.getSerializableExtra("article") as? ArticlesItem

        // Atur nilai untuk komponen UI menggunakan ViewBinding
        binding.title.text = article?.title
        binding.description.text = article?.description
        binding.publishedAt.text = article?.publishedAt
        binding.author.text = "Author: ${article?.author ?: "Unknown"}"
        binding.content.text = article?.content

        article?.urlToImage?.let {
            Glide.with(this).load(it).into(binding.image)
        }

        // Menambahkan listener untuk membuka artikel di browser
        binding.openInBrowser.setOnClickListener {
            article?.url?.let { url ->
                val browserIntent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
                startActivity(browserIntent)  // Membuka browser dengan URL artikel
            }
        }

        // Menambahkan listener untuk tombol close
        binding.closeButton.setOnClickListener {
            finish()  // Menutup aktivitas saat tombol close ditekan
        }
    }
}