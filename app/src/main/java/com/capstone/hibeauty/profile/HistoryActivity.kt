package com.capstone.hibeauty.profile

import android.widget.Toast
import com.capstone.hibeauty.adapter.HistoryAdapter
import com.capstone.hibeauty.adapter.Prediction
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.hibeauty.R
import com.capstone.hibeauty.databinding.ActivityHistoryBinding
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchSkinHistory()

        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun fetchSkinHistory() {
        val token = SharedPreferenceUtil.getToken(this)
        val sharedPreferences = getSharedPreferences("HiBeautyPreferences", MODE_PRIVATE)
        val skinId = sharedPreferences.getString("SKINID", null)

        if (skinId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val url = URL("https://backend-q4bx5v5sia-et.a.run.app/predict/$skinId")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Authorization", "Bearer $token")

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val response = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
                        Log.d("Response", "Response: $response")  // Log the response for debugging

                        val jsonResponse = JSONObject(response)
                        if (jsonResponse.getBoolean("status")) {
                            val data = jsonResponse.getJSONArray("data")
                            val predictions = mutableListOf<Prediction>()
                            for (i in 0 until data.length()) {
                                val item = data.getJSONObject(i)
                                val skinType = item.getString("skinType")
                                val percentage = item.getDouble("percentage").toFloat()
                                predictions.add(Prediction(skinType, percentage))
                            }
                            withContext(Dispatchers.Main) {
                                adapter = HistoryAdapter(predictions)
                                binding.historyRecyclerView.adapter = adapter
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                showToast("Failed to retrieve history")
                            }
                        }
                    } else {
                        Log.e("FetchError", "Failed to fetch history, response code: $responseCode")
                        withContext(Dispatchers.Main) {
                            showToast("Failed to fetch history")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        showToast("Error: ${e.message}")
                    }
                }
            }
        } else {
            showToast("No SKINID found in preferences")
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
