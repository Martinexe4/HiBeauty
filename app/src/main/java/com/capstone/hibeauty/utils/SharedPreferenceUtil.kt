package com.capstone.hibeauty.utils

import android.content.Context

object SharedPreferenceUtil {

    private const val PREF_NAME = "HiBeautyPrefs"
    private const val KEY_TOKEN = "TOKEN"
    private const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
    private const val KEY_USERNAME = "USERNAME"
    private const val KEY_USER_ID = "USER_ID"
    private const val KEY_PERSONALIZATION_COMPLETED = "PERSONALIZATION_COMPLETED"
    private const val KEY_PROFILE_IMAGE_URL = "profile_image_url"

    fun saveToken(context: Context, token: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }

    fun clearToken(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_TOKEN)
            .apply()
    }

    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUsername(context: Context, username: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_USERNAME, username)
            .apply()
    }

    fun getUsername(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, null)
    }

    fun saveUserId(context: Context, userId: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString("USERID", userId)
            .apply()
    }

    fun getUserId(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString("USERID", null)
    }

    fun savePersonalizationCompleted(context: Context, completed: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_PERSONALIZATION_COMPLETED, completed)
            .apply()
    }

    fun isPersonalizationCompleted(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_PERSONALIZATION_COMPLETED, false)
    }

    fun clearUserData(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_TOKEN)
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_USERNAME)
            .remove(KEY_USER_ID)
            .apply()
    }

    fun saveProfileImageUrl(context: Context, url: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PROFILE_IMAGE_URL, url).apply()
    }

    fun getProfileImageUrl(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PROFILE_IMAGE_URL, null)
    }

    // Add other methods as needed
}
