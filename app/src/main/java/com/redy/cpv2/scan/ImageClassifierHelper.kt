package com.redy.cpv2.scan

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class ImageClassifierHelper(private val context: Context) {
    private val classifier: ImageClassifier =
        ImageClassifier.createFromFile(context, "cancer_classification.tflite")

    @Suppress("DEPRECATION")
    fun classifyStaticImage(uri: Uri): Pair<String, Float> {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val image = TensorImage.fromBitmap(bitmap)
        val results = classifier.classify(image)
        val category = results[0].categories.first()
        return Pair(category.label, category.score)
    }
}