package com.capstone.hibeauty.profile

import android.content.Context

class LanguagePreference(context: Context) {
    val preferences = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE)

    fun loadSettingLanguage() : String? {
        return preferences.getString(LANGUAGE_KEY, "en")
    }

    fun saveSettingLanguage(language: String) {
        preferences.edit().putString(LANGUAGE_KEY, language).apply()
    }

    companion object {
        const val PREFRENCES_NAME = "language_preference"
        const val LANGUAGE_KEY = "language_key"
    }
}