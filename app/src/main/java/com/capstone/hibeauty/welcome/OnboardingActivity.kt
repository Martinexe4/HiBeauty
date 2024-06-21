package com.capstone.hibeauty.welcome

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.capstone.hibeauty.R
import com.capstone.hibeauty.adapter.OnboardingAdapter
import com.capstone.hibeauty.authentication.LoginActivity
import com.capstone.hibeauty.databinding.ActivityOnboardingBinding
import com.capstone.hibeauty.utils.LanguageUtils

@Suppress("DEPRECATION")
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val delay: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val hasOnboarded = sharedPreferences.getBoolean("has_onboarded", false)
        if (hasOnboarded) {
            navigateToLogin()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsetsCompat.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val language = LanguageUtils.getLanguagePreference(this)
        if (language != null) {
            LanguageUtils.setLocale(this, language)
        }

        val viewPager = binding.viewPager
        val indicatorLayout = binding.indicatorLayout
        val buttonStart = binding.buttonStart
        val btnLogin = binding.loginButton

        val adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            var currentPage = 0

            override fun run() {
                if (currentPage >= adapter.itemCount) {
                    currentPage = 0
                }
                viewPager.setCurrentItem(currentPage, true)
                currentPage++
                handler.postDelayed(this, delay)
            }
        }

        handler.postDelayed(runnable, delay)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                for (i in 0 until indicatorLayout.childCount) {
                    val indicator = indicatorLayout.getChildAt(i)
                    if (i == position) {
                        indicator.setBackgroundColor(resources.getColor(R.color.colorPrimary))  // Active color
                    } else {
                        indicator.setBackgroundColor(resources.getColor(R.color.colorGrayLight))  // Inactive color
                    }
                }
            }
        })

        buttonStart.text = getString(R.string.start_button)
        buttonStart.setOnClickListener {
            handler.removeCallbacks(runnable)
            sharedPreferences.edit().putBoolean("has_onboarded", true).apply()
            navigateToLogin()
        }
        btnLogin.setOnClickListener {
            handler.removeCallbacks(runnable)
            sharedPreferences.edit().putBoolean("has_onboarded", true).apply()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
        finish()
    }
}