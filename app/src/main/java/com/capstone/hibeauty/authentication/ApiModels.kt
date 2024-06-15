package com.capstone.hibeauty.authentication

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
    val id: String,
    val predictions: List<Prediction>
)

data class Prediction(
    val id: Int,
    val percentage: Double
)