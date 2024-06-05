package com.redy.cpv2.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val imageResId: Int,
    val name: String,
    val price: String,
    val description: String,
    val rating: Float,
    val imageList: List<Int>
) : Parcelable