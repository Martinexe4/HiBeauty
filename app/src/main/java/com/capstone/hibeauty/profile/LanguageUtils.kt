package com.capstone.hibeauty.profile

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

object LanguageUtils {
    fun setLanguagePreference(context: Context, language: String) {
        val sharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selected_language", language)
        editor.apply()
    }

    fun getLanguagePreference(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("selected_language", null)
    }

    fun setLocale(activity: AppCompatActivity, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
    }
}