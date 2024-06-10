package com.capstone.hibeauty.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.databinding.ActivityLanguageBinding
import java.util.Locale

class LanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.languageEnglish.setOnClickListener {
            setLocale("en")
        }

        binding.languageIndonesian.setOnClickListener {
            setLocale("id")
        }

        loadLocale()
    }

    private fun setLocale(languageCode: String) {
        val editor = sharedPreferences.edit()
        editor.putString("selected_language", languageCode)
        editor.apply()

        // Update the app's locale
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Refresh activity to apply the new language
        finish()
        startActivity(intent)
    }

    private fun loadLocale() {
        val languageCode = sharedPreferences.getString("selected_language", "en")
        val locale = Locale(languageCode ?: "en")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}