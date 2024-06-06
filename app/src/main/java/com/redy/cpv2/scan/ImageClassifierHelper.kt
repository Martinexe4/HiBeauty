package com.redy.cpv2.scan

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.redy.cpv2.ml.SkinDiseaseModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageClassifierHelper(private val context: Context) {

    fun classifyStaticImage(uri: Uri): Map<String, Float> {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resizedBitmap)
        val byteBuffer = tensorImage.buffer

        val model = SkinDiseaseModel.newInstance(context)
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val acneScore = outputFeature0.floatArray[0]
        val eyeBagsScore = outputFeature0.floatArray[1]
        val oilyScore = outputFeature0.floatArray[2]

        model.close()

        return mapOf(
            "Acne" to acneScore,
            "Eye Bags" to eyeBagsScore,
            "Oily" to oilyScore
        )
    }
}