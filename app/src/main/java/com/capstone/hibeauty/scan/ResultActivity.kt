package com.capstone.hibeauty.scan

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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
            saveResultToDatabase()
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

    private fun saveResultToDatabase() {
        val url = "https://backend-q4bx5v5sia-et.a.run.app/predictions"
        val skinId = 3 // Example skinId, you may change it according to your needs
        val jsonBody = JSONObject()
        jsonBody.put("skinId", skinId)

        val predictionsArray = JSONArray()
        results?.forEach { (key, value) ->
            val predictionObject = JSONObject()
            predictionObject.put("id", getIdForKey(key)) // You need to implement getIdForKey function
            predictionObject.put("percentage", value)
            predictionsArray.put(predictionObject)
        }
        jsonBody.put("predictions", predictionsArray)

        // Print the payload to logcat for debugging
        println("Payload: $jsonBody")

        CoroutineScope(Dispatchers.IO).launch {
            val token = SharedPreferenceUtil.getToken(this@ResultActivity)
            if (token == null) {
                withContext(Dispatchers.Main) {
                    showToast("Authentication token is missing.")
                }
                return@launch
            }

            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                urlConnection.setRequestProperty("Authorization", "Bearer $token")
                urlConnection.doOutput = true

                val outputStreamWriter = OutputStreamWriter(urlConnection.outputStream)
                outputStreamWriter.write(jsonBody.toString())
                outputStreamWriter.flush()
                outputStreamWriter.close()

                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage

                if (responseCode in 200..299) {
                    withContext(Dispatchers.Main) {
                        showToast("Results saved successfully.")
                    }
                } else {
                    val errorStream = BufferedReader(InputStreamReader(urlConnection.errorStream))
                    val errorResponse = StringBuilder()
                    var line: String?
                    while (errorStream.readLine().also { line = it } != null) {
                        errorResponse.append(line)
                    }
                    errorStream.close()

                    withContext(Dispatchers.Main) {
                        showToast("Failed to save results: $responseCode $responseMessage")
                        showToast("Error details: $errorResponse")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Error: ${e.message}")
                }
            }
        }
    }

    private fun getIdForKey(key: String): Int {
        return when (key) {
            "acne" -> 1
            "eye bags" -> 2
            "oily" -> 3
            else -> -1 // Handle unknown keys appropriately
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}