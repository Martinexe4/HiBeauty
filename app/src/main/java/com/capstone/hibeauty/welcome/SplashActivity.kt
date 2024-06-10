package com.capstone.hibeauty.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import com.capstone.hibeauty.MainActivity
import com.capstone.hibeauty.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsetsCompat.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

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

//    private fun goToPersonalizationActivity(){
//        Intent(this, PersonalizationActivity::class.java).also{
//            startActivity(it)
//            finish()
//        }
//    }

    private fun goToOnboardingActivity(){
        Intent(this, OnboardingActivity::class.java).also{
            startActivity(it)
            finish()
        }
    }
}