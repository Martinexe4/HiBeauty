package com.capstone.hibeauty.utils

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class InitApp : Application() {
    private var isNightModeEnabled = false
    override fun onCreate() {
        super.onCreate()
        singleton = this
        val mPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        isNightModeEnabled = mPrefs.getBoolean(NIGHT_MODE, false)
    }

    fun isNightModeEnabled(): Boolean {
        return isNightModeEnabled
    }

    companion object {
        const val NIGHT_MODE = "NIGHT_MODE"
        private var singleton: InitApp? = null
    }
}