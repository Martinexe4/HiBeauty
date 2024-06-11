package com.capstone.hibeauty.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.hibeauty.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)



        binding.closeButton.setOnClickListener {
            finish()
        }

        loadHistory()
    }

    private fun loadHistory() {

    }

    private fun deleteHistoryItem(id: String) {

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "HistoryActivity"
    }
}