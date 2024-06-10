package com.capstone.hibeauty.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.ui.profile.ProfileFragment

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_profile)  // Layout dengan kontainer Fragment

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())  // Mengganti konten dengan ProfileFragment
                .commit()
        }
    }
}