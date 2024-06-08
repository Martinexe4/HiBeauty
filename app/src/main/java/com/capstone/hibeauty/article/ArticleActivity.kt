package com.capstone.hibeauty.article

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R

class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article) // Layout yang memuat fragment_container

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ArticleFragment())
                .commit()
        }
    }
}