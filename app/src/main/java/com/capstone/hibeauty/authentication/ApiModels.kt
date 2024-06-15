package com.capstone.hibeauty.authentication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmpassword: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? // Add other fields as necessary
)

data class HistoryItem(
    val id: String, // Assuming id is a String
    val predictions: List<Prediction>
)

data class Prediction(
    val skinType: String,
    val percentage: Float
)

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val ingridients: String,
    val link: String,
    val recomId: Int,
    val typeId: Int
) : Parcelable

data class ProductResponse(
    val products: List<Product>
)
