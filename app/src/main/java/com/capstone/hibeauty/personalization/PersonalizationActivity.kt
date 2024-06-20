package com.capstone.hibeauty.personalization

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.capstone.hibeauty.MainActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.adapter.PersonalizationAdapter
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.AgeGenderRequest
import com.capstone.hibeauty.api.AgeGenderResponse
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalizationActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: PersonalizationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalization)

        supportActionBar?.hide()

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val fragments = listOf(AgeFragment(), GenderFragment())
        adapter = PersonalizationAdapter(this, fragments)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_age)
                1 -> getString(R.string.tab_gender)
                else -> null
            }
        }.attach()
    }

    fun goToNextPage() {
        if (viewPager.currentItem < adapter.itemCount - 1) {
            viewPager.currentItem += 1
        }
    }

    fun finishPersonalization() {
        val ageFragment = adapter.createFragment(0) as AgeFragment
        val genderFragment = adapter.createFragment(1) as GenderFragment

        val age = ageFragment.getAge()
        val gender = genderFragment.getGender()

        saveUserData(age, gender)
    }

    private fun saveUserData(age: Int, gender: String) {
        val token = SharedPreferenceUtil.getToken(this) ?: ""
        val userId = SharedPreferenceUtil.getUserId(this) ?: ""
        val request = AgeGenderRequest(AGE = age, GENDER = gender)

        val call = ApiConfig.apiService.updateUserAgeGender("Bearer $token", userId, request)

        call.enqueue(object : Callback<AgeGenderResponse> {
            override fun onResponse(call: Call<AgeGenderResponse>, response: Response<AgeGenderResponse>) {
                if (response.isSuccessful) {
                    SharedPreferenceUtil.savePersonalizationCompleted(this@PersonalizationActivity, true)
                    goToMainActivity()
                } else {
                    Toast.makeText(this@PersonalizationActivity, getString(R.string.failed_to_update_data), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AgeGenderResponse>, t: Throwable) {
                Toast.makeText(this@PersonalizationActivity, getString(R.string.failed_to_connect_server), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}