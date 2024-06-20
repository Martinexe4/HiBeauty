package com.capstone.hibeauty.utils

import android.content.Context
import java.io.File

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(null)
    return File.createTempFile(
        "JPEG_${System.currentTimeMillis()}_",
        ".jpg",
        storageDir
    )
}