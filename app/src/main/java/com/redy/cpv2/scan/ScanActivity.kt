package com.redy.cpv2.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.redy.cpv2.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null

    // ML
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        handleIntent()

        imageClassifierHelper = ImageClassifierHelper(this)

        binding.uploadButton.setOnClickListener {
            currentImageUri?.let { uri ->
                startLoadingAnimation()

                analyzeImage(uri) { results ->
                    runOnUiThread {
                        // Ensure loading animation is hidden
                        binding.darkOverlayView.visibility = android.view.View.GONE
                        binding.loadingPercentageTextView.visibility = android.view.View.GONE

                        // Move to result activity
                        moveToResult(uri, results)
                    }
                }
            } ?: showToast("Pilih gambar terlebih dahulu")
        }

        binding.backButton.setOnClickListener {
            finish() // Finish the activity and return to the previous screen
        }
    }

    private fun handleIntent() {
        val imageUriString = intent.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
        if (imageUriString != null) {
            currentImageUri = imageUriString.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(imageUri: Uri, callback: (Map<String, Float>) -> Unit) {
        if (isAllowedImageType(imageUri)) {
            val results = imageClassifierHelper.classifyStaticImage(imageUri)
            callback(results)
        } else {
            showToast("File harus berupa PNG, JPG, atau JPEG")
        }
    }

    private fun isAllowedImageType(uri: Uri): Boolean {
        val contentResolver = contentResolver
        val type = contentResolver.getType(uri)
        return type?.let {
            it == "image/png" || it == "image/jpeg" || it == "image/jpg"
        } ?: false
    }

    private fun moveToResult(imageUri: Uri, results: Map<String, Float>) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("imageUri", imageUri.toString())
            putExtra("results", HashMap(results))
        }
        startActivity(intent)
    }

    private fun startLoadingAnimation() {
        binding.darkOverlayView.visibility = android.view.View.VISIBLE
        binding.loadingPercentageTextView.visibility = android.view.View.VISIBLE

        val handler = Handler(Looper.getMainLooper())
        var progress = 0

        handler.post(object : Runnable {
            override fun run() {
                if (progress <= 100) {
                    binding.loadingPercentageTextView.text = "$progress%"
                    progress++
                    handler.postDelayed(this, 50) // Update progress every 50ms
                } else {
                    handler.removeCallbacks(this)
                    // Loading animation complete
                    binding.darkOverlayView.visibility = android.view.View.GONE
                    binding.loadingPercentageTextView.visibility = android.view.View.GONE
                }
            }
        })
    }

    private fun startResultActivity() {
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish ScanActivity if you don't want to return to it
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}