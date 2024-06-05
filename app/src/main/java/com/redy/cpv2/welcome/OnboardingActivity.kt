package com.redy.cpv2.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.redy.cpv2.MainActivity
import com.redy.cpv2.R
import com.redy.cpv2.authentication.LoginActivity
import com.redy.cpv2.databinding.ActivityOnboardingBinding
import com.redy.cpv2.profile.LanguageUtils

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val delay: Long = 3000  // Time for slide shift (3 seconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if onboarding has already been completed
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val hasOnboarded = sharedPreferences.getBoolean("has_onboarded", false)
        if (hasOnboarded) {
            navigateToLogin()
            return
        }

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Language setting
        val language = LanguageUtils.getLanguagePreference(this)
        if (language != null) {
            LanguageUtils.setLocale(this, language) // Apply language
        }

        val viewPager = binding.viewPager
        val indicatorLayout = binding.indicatorLayout
        val buttonStart = binding.buttonStart

        // Set adapter for ViewPager2
        val adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter

        // Create Handler and Runnable
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            var currentPage = 0

            override fun run() {
                if (currentPage >= adapter.itemCount) {
                    currentPage = 0  // Return to the first page if exceeding the number of pages
                }
                viewPager.setCurrentItem(currentPage, true)  // Move to the next page
                currentPage++
                handler.postDelayed(this, delay)  // Run again after 3 seconds
            }
        }

        // Start running the runnable
        handler.postDelayed(runnable, delay)

        // Logic to update indicators based on the selected page
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // Update indicators based on the position of ViewPager2
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

        // Start button to skip onboarding and go to LoginActivity
        buttonStart.text = "Start"
        buttonStart.setOnClickListener {
            // Stop handler and move to another activity
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