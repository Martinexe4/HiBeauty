package com.redy.cpv2.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redy.cpv2.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)  // Layout dengan kontainer Fragment

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())  // Mengganti konten dengan ProfileFragment
                .commit()
        }
    }
}