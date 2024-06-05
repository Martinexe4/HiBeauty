package com.redy.cpv2.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.redy.cpv2.MainActivity
import com.redy.cpv2.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            goToOnboardingActivity()
        }, 3000L)
    }

    private fun goToMainActivity(){
        Intent(this, MainActivity::class.java).also{
            startActivity(it)
            finish()
        }
    }

    private fun goToOnboardingActivity(){
        Intent(this, OnboardingActivity::class.java).also{
            startActivity(it)
            finish()
        }
    }
}