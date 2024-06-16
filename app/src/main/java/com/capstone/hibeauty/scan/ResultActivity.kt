package com.capstone.hibeauty.scan

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.capstone.hibeauty.databinding.ActivityResultBinding
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var imageUri: Uri? = null
    private var results: Map<String, Float>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        imageUri = intent.getStringExtra("imageUri")?.toUri()
        results = intent.getSerializableExtra("results") as? Map<String, Float>

        displayResult()

        binding.saveResultButton.setOnClickListener {
            saveImageToDatabase()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun displayResult() {
        imageUri?.let {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
            binding.resultImageView.setImageBitmap(bitmap)
        }

        results?.let {
            val resultsText = it.entries.joinToString("\n") { entry ->
                "${entry.key}: ${String.format("%.2f%%", entry.value * 100)}"
            }
            binding.resultTextView?.text = resultsText
        }
    }

    private fun saveImageToDatabase() {
        imageUri?.let { uri ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val url = URL("https://backend-q4bx5v5sia-et.a.run.app/skin/upload")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Authorization", "Bearer <your_token_here>")
                    connection.doOutput = true
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=boundary")

                    val boundary = "boundary"
                    val LINE_FEED = "\r\n"

                    connection.outputStream.use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writer.append("--$boundary").append(LINE_FEED)
                            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"image.jpg\"")
                                .append(LINE_FEED)
                            writer.append("Content-Type: image/jpeg").append(LINE_FEED)
                            writer.append(LINE_FEED).flush()

                            contentResolver.openInputStream(uri)?.use { inputStream ->
                                inputStream.copyTo(outputStream)
                            }
                            outputStream.flush()

                            writer.append(LINE_FEED).flush()
                            writer.append("--$boundary--").append(LINE_FEED).flush()
                        }
                    }

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val response = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
                        Log.d("Response", "Response: $response")  // Log the response for debugging

                        val jsonResponse = JSONObject(response)
                        if (jsonResponse.has("data")) {
                            val data = jsonResponse.getJSONObject("data")
                            if (data.has("SKINID")) {
                                val skinId = data.getString("SKINID")
                                Log.d("SKINID", "SKINID: $skinId")
                                savePredictionToDatabase(skinId)
                            } else {
                                withContext(Dispatchers.Main) {
                                    showToast("No SKINID found in response data")
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                showToast("No data found in response")
                            }
                        }
                    } else {
                        Log.e("UploadError", "Failed to upload image, response code: $responseCode")
                        withContext(Dispatchers.Main) {
                            showToast("Failed to upload image")
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
    }

    private fun savePredictionToDatabase(skinId: String) {
        val token = SharedPreferenceUtil.getToken(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://backend-q4bx5v5sia-et.a.run.app/predictions")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val predictionMap = mapOf(
                    "acne" to 1,
                    "oily" to 2,
                    "eye bags" to 3
                )

                val jsonBody = JSONObject().apply {
                    put("skinId", skinId)
                    put("predictions", JSONArray().apply {
                        results?.forEach { (name, percentage) ->
                            val id = predictionMap[name] ?: error("Unknown prediction name: $name")
                            put(JSONObject().apply {
                                put("id", id)
                                put("percentage", percentage)
                            })
                        }
                    })
                }

                Log.d("SavePredictionRequest", "Request Body: $jsonBody") // Log the request body

                OutputStreamWriter(connection.outputStream).use { it.write(jsonBody.toString()) }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
                    Log.d("SavePredictionResponse", "Response: $response")  // Log the response for debugging
                    withContext(Dispatchers.Main) {
                        showToast("Prediction saved successfully")
                    }
                } else {
                    val errorResponse = BufferedReader(InputStreamReader(connection.errorStream)).use { it.readText() }
                    Log.e("SavePredictionError", "Failed to save prediction, response code: $responseCode, error: $errorResponse")
                    withContext(Dispatchers.Main) {
                        showToast("Failed to save prediction")
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
