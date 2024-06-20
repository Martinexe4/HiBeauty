package com.capstone.hibeauty.profile


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.databinding.ActivityPolicyBinding
import android.text.method.LinkMovementMethod
import androidx.core.text.HtmlCompat

class PolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.privacyPolicyContentTextView.text = HtmlCompat.fromHtml(
            getString(R.string.privacy_policy_content),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.privacyPolicyContentTextView.movementMethod = LinkMovementMethod.getInstance()

        binding.closeButton.setOnClickListener {
            finish()
        }
    }
}