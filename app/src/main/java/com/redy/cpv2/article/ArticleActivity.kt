package com.redy.cpv2.article

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redy.cpv2.R

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