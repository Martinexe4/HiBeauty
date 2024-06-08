package com.capstone.hibeauty.scan

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.hibeauty.R
import com.capstone.hibeauty.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private var imageCapture: ImageCapture? = null
    private var currentFlashMode = FLASH_MODE_OFF
    private val handler = Handler(Looper.getMainLooper())
    private val textMessages = listOf(
        "Pastikan pencahayaan di sekitar anda baik",
        "Sesuaikan wajah dengan frame kamera",
        "Gunakan ekspresi wajah netral"
    )
    private var textIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.openGallery.setOnClickListener { startGallery() }
        binding.flashToggle.setOnClickListener { toggleFlash() }
        binding.backButton.setOnClickListener { onBackPressed() }

        startTextSwitcher()
    }

    private fun startTextSwitcher() {
        handler.post(object : Runnable {
            override fun run() {
                binding.textInfo.text = textMessages[textIndex]
                textIndex = (textIndex + 1) % textMessages.size
                handler.postDelayed(this, 2000)
            }
        })
    }

    private fun toggleFlash() {
        currentFlashMode = when (currentFlashMode) {
            FLASH_MODE_OFF -> FLASH_MODE_ON
            FLASH_MODE_ON -> FLASH_MODE_AUTO
            FLASH_MODE_AUTO -> FLASH_MODE_OFF
            else -> FLASH_MODE_OFF
        }
        updateFlashIcon()
        imageCapture?.flashMode = currentFlashMode
    }

    private fun updateFlashIcon() {
        val icon = when (currentFlashMode) {
            FLASH_MODE_OFF -> R.drawable.ic_flash_off
            FLASH_MODE_ON -> R.drawable.ic_flash_on
            FLASH_MODE_AUTO -> R.drawable.ic_flash_auto
            else -> R.drawable.ic_flash_off
        }
        binding.flashToggle.setImageResource(icon)
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0) // Ensure correct rotation
                .setFlashMode(currentFlashMode)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Failed to start camera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val compressedPhotoFile = photoFile.reduceFileImage()
                    val savedUri = Uri.fromFile(compressedPhotoFile)
                    val intent = Intent(this@CameraActivity, ScanActivity::class.java).apply {
                        putExtra(EXTRA_CAMERAX_IMAGE, savedUri.toString())
                    }
                    startActivity(intent)
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Failed to capture image.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val intent = Intent(this, ScanActivity::class.java).apply {
                putExtra(EXTRA_CAMERAX_IMAGE, uri.toString())
            }
            startActivity(intent)
            finish()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture?.targetRotation = rotation
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    companion object {
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200

        private const val FLASH_MODE_ON = ImageCapture.FLASH_MODE_ON
        private const val FLASH_MODE_OFF = ImageCapture.FLASH_MODE_OFF
        private const val FLASH_MODE_AUTO = ImageCapture.FLASH_MODE_AUTO

        fun createCustomTempFile(context: android.content.Context): File {
            val storageDir: File? = context.getExternalFilesDir(null)
            return File.createTempFile(
                "JPEG_${System.currentTimeMillis()}_",
                ".jpg",
                storageDir
            )
        }
    }
}
