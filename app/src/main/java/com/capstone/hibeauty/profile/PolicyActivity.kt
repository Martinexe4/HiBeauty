package com.capstone.hibeauty.profile


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.databinding.ActivityPolicyBinding

class PolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Mengisi TextView dengan kebijakan privasi
        binding.privacyPolicyContentTextView.text = getString(R.string.privacy_policy_content)


        // Menutup activity saat tombol "X" diklik
        binding.closeButton.setOnClickListener {
            finish()
        }
    }
}