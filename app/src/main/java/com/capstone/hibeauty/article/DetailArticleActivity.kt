package com.capstone.hibeauty.article

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.capstone.hibeauty.R

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var publishedAt: TextView
    private lateinit var author: TextView
    private lateinit var image: ImageView
    private lateinit var content: TextView
    private lateinit var openInBrowserButton: Button
    private lateinit var closeButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)

        // Temukan komponen UI
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        publishedAt = findViewById(R.id.published_at)
        author = findViewById(R.id.author)
        image = findViewById(R.id.image)
        content = findViewById(R.id.content)
        openInBrowserButton = findViewById(R.id.open_in_browser)
        closeButton = findViewById(R.id.close_button)

        // Terima data artikel dari Intent
        val article = intent.getSerializableExtra("article") as? ArticlesItem

        // Atur nilai untuk komponen UI
        title.text = article?.title
        description.text = article?.description
        publishedAt.text = article?.publishedAt
        author.text = "Author: ${article?.author ?: "Unknown"}"
        content.text = article?.content

        article?.urlToImage?.let {
            Glide.with(this).load(it).into(image)
        }

        // Menambahkan listener untuk membuka artikel di browser
        openInBrowserButton.setOnClickListener {
            article?.url?.let { url ->
                val browserIntent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
                startActivity(browserIntent)  // Membuka browser dengan URL artikel
            }
        }

        // Menambahkan listener untuk tombol close
        closeButton.setOnClickListener {
            finish()  // Menutup aktivitas saat tombol close ditekan
        }
    }
}