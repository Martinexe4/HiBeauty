package com.capstone.hibeauty.scan

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.capstone.hibeauty.ml.SkinDiseaseModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassifierHelper(private val context: Context) {

    private val model = SkinDiseaseModel.newInstance(context)

    @Suppress("DEPRECATION")
    fun classifyStaticImage(uri: Uri): Map<String, Float> {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        // Normalize the image data to [0, 1] and convert to ByteBuffer
        val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val results = mapOf(
            "acne" to outputFeature0.floatArray[0],
            "eye bags" to outputFeature0.floatArray[1],
            "oily" to outputFeature0.floatArray[2]
        )

        model.close()

        return results
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(224 * 224)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until 224) {
            for (j in 0 until 224) {
                val value = intValues[pixel++]
                // Normalize the pixel values to [0, 1]
                byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value and 0xFF) / 255.0f))
            }
        }
        return byteBuffer
    }
}