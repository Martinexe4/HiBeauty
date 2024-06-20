package com.capstone.hibeauty.profile

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.databinding.ActivityLanguageBinding
import kotlin.system.exitProcess

class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    private lateinit var preference: LanguagePreference
    private lateinit var languageList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preference = LanguagePreference(this)

        languageList = resources.getStringArray(R.array.language_list)

        binding.languageSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, languageList)

        val lang = preference.loadSettingLanguage()
        val index = languageList.indexOf(lang)

        if (index >= 0) {
            binding.languageSpinner.setSelection(index)
        }

        binding.btnApply.setOnClickListener {
            val savedInstance: String = when (binding.languageSpinner.selectedItemPosition) {
                0 -> "en"
                1 -> "in"
                else -> "unknown"
            }
            showConfirmationDialog(
                savedInstance,
                languageList[binding.languageSpinner.selectedItemPosition]
            )
        }

        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun showConfirmationDialog(languageCode: String, languageName: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.change_language_title)
            .setMessage("${getString(R.string.change_language_message)} ${languageName}?")
            .setPositiveButton(R.string.choose_yes) { _, _ ->
                preference.saveSettingLanguage(languageCode)
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.language_changed_alert))
                    .setMessage(
                        "${getString(R.string.language_message_1)} ${languageName}${
                            getString(
                                R.string.language_message_2
                            )
                        }"
                    )
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        exitProcess(0)
                    }
                    .setCancelable(false)
                    .show()
            }
            .setNegativeButton(R.string.choose_no, null)
            .show()
    }
}