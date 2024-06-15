package com.capstone.hibeauty.scan

import android.content.Context
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
        // Ambil token dari SharedPreferenceUtil
        val token = SharedPreferenceUtil.getToken(this)

        // Pastikan token tidak kosong atau null sebelum menggunakan
        if (token.isNullOrEmpty()) {
            showToast("Token not found, please login again")
            return
        }

        // Create JSON payload
        val jsonObject = JSONObject().apply {
            put("skinId", "3") // Sesuaikan skinId sesuai kebutuhan
            val predictionsArray = JSONArray()
            results?.forEach { (key, value) ->
                val prediction = JSONObject().apply {
                    put("id", key) // Menggunakan key sebagai string
                    put("percentage", value)
                }
                predictionsArray.put(prediction)
            }
            put("predictions", predictionsArray)
        }

        // Coroutine untuk menjalankan operasi jaringan di thread background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://backend-q4bx5v5sia-et.a.run.app/predictions")
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json; utf-8")
                    setRequestProperty("Authorization", "Bearer $token") // Gunakan token dari SharedPreferenceUtil
                    doOutput = true
                }

                connection.outputStream.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        writer.write(jsonObject.toString())
                        writer.flush()
                    }
                }

                val responseCode = connection.responseCode
                val responseBody = connection.inputStream.bufferedReader().use { it.readText() }

                withContext(Dispatchers.Main) {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        showToast("Results saved successfully!")
                    } else {
                        showToast("Failed to save results: $responseCode")
                        // Tambahkan log untuk respons dari server
                        println("Response body: $responseBody")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("An error occurred: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}