package com.capstone.hibeauty.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.databinding.ActivityLanguageBinding
import com.capstone.hibeauty.ui.profile.ProfileFragment
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
            showConfirmationDialog("en")
        }

        binding.languageIndonesian.setOnClickListener {
            showConfirmationDialog("id")
        }

        loadLocale()
    }

    private fun showConfirmationDialog(languageCode: String) {
        val languageName = if (languageCode == "en") "English" else "Indonesian"
        AlertDialog.Builder(this)
            .setTitle("Change Language")
            .setMessage("Are you sure you want to change the language to $languageName?")
            .setPositiveButton("Yes") { _, _ ->
                setLocale(languageCode)
                // Navigate to ProfileFragment
                navigateToProfileFragment()
            }
            .setNegativeButton("No", null)
            .show()
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

    private fun navigateToProfileFragment() {
        val fragment = ProfileFragment() // Replace with the actual fragment initialization if needed
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
    }
}