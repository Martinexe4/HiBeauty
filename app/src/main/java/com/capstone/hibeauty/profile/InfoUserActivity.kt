package com.capstone.hibeauty.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.hibeauty.R
import com.capstone.hibeauty.databinding.ActivityInfoUserBinding
import com.capstone.hibeauty.databinding.DialogEditUserBinding

class InfoUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoUserBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        loadUserData()

        binding.btnEdit.setOnClickListener {
            showEditDialog()
        }
    }

    private fun loadUserData() {
        val name = sharedPreferences.getString("name", "")
        val age = sharedPreferences.getString("age", "")
        val gender = sharedPreferences.getString("gender", "")

        binding.tvName.text = "Name: $name"
        binding.tvAge.text = "Age: $age"
        binding.tvGender.text = "Gender: $gender"
    }

    private fun showEditDialog() {
        val dialogBinding = DialogEditUserBinding.inflate(LayoutInflater.from(this))

        dialogBinding.etName.setText(sharedPreferences.getString("name", ""))
        dialogBinding.etAge.setText(sharedPreferences.getString("age", ""))
        when (sharedPreferences.getString("gender", "")) {
            "Male" -> dialogBinding.rbMale.isChecked = true
            "Female" -> dialogBinding.rbFemale.isChecked = true
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit User Info")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val name = dialogBinding.etName.text.toString()
                val age = dialogBinding.etAge.text.toString()
                val gender = when (dialogBinding.rgGender.checkedRadioButtonId) {
                    R.id.rb_male -> "Male"
                    R.id.rb_female -> "Female"
                    else -> ""
                }

                saveUserData(name, age, gender)
                loadUserData()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun saveUserData(name: String, age: String, gender: String) {
        with(sharedPreferences.edit()) {
            putString("name", name)
            putString("age", age)
            putString("gender", gender)
            apply()
        }
    }
}