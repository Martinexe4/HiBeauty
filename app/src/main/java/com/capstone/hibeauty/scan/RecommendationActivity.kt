package com.capstone.hibeauty.scan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.hibeauty.adapter.RecommendationAdapter
import com.capstone.hibeauty.api.Recommendation
import com.capstone.hibeauty.databinding.ActivityRecommendationBinding
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

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    private lateinit var recommendationAdapter: RecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val skinId = intent.getStringExtra("skinId") ?: run {
            showToast("No SKINID provided")
            finish()
            return
        }

        setupRecyclerView()
        fetchRecommendations(skinId)
    }

    private fun setupRecyclerView() {
        recommendationAdapter = RecommendationAdapter()
        binding.recommendationRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RecommendationActivity)
            adapter = recommendationAdapter
        }
    }

    private fun fetchRecommendations(skinId: String) {
        val token = SharedPreferenceUtil.getToken(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://backend-q4bx5v5sia-et.a.run.app/recommendations/$skinId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer $token")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
                    Log.d("RecommendationResponse", "Response: $response")  // Log the response for debugging

                    val jsonResponse = JSONObject(response)
                    val recommendations = jsonResponse.getJSONArray("recommendations") // Adjust this according to actual response structure

                    val recommendationList = mutableListOf<Recommendation>()
                    for (i in 0 until recommendations.length()) {
                        val item = recommendations.getJSONObject(i)
                        val name = item.getString("name")
                        val description = item.getString("description") // Adjust according to actual response fields
                        recommendationList.add(Recommendation(name, description))
                    }

                    withContext(Dispatchers.Main) {
                        recommendationAdapter.submitList(recommendationList)
                    }
                } else {
                    Log.e("FetchError", "Failed to fetch recommendations, response code: $responseCode")
                    withContext(Dispatchers.Main) {
                        showToast("Failed to fetch recommendations")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("Error: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}