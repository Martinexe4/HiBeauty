package com.capstone.hibeauty.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.hibeauty.adapter.HistoryAdapter
import com.capstone.hibeauty.authentication.HistoryItem
import com.capstone.hibeauty.authentication.LoginActivity
import com.capstone.hibeauty.authentication.Prediction
import com.capstone.hibeauty.databinding.ActivityHistoryBinding
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(emptyList())
        binding.recyclerViewHistory.adapter = historyAdapter

        binding.closeButton.setOnClickListener {
            finish()
        }

        loadHistory()
    }

    private fun loadHistory() {
        val token = SharedPreferenceUtil.getToken(this)
        if (token.isNullOrEmpty()) {
            showToast("Token not found. Please login again.")
            navigateToLogin()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://backend-q4bx5v5sia-et.a.run.app/predict/1")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer $token")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseBody = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(responseBody)

                    if (jsonResponse.getBoolean("status")) {
                        val jsonArray = jsonResponse.getJSONArray("data")
                        val historyItems = mutableListOf<HistoryItem>()

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val skinType = jsonObject.getString("skinType")
                            val percentage = jsonObject.getDouble("percentage")
                            val predictionList = mutableListOf<Prediction>(Prediction(skinType, percentage.toFloat()))
                            historyItems.add(HistoryItem(skinType, predictionList))
                        }

                        runOnUiThread {
                            historyAdapter.updateData(historyItems)
                        }
                    } else {
                        showToast("Failed to load history: ${jsonResponse.getString("message")}")
                    }
                } else {
                    showToast("Failed to load history: $responseCode")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("An error occurred: ${e.message}")
            }
        }
    }

    private fun deleteHistoryItem(id: String) {
        // Implementation for deleting a history item
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "HistoryActivity"
    }
}