package com.capstone.hibeauty.authentication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

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

@Parcelize
data class ProductType(
    val id: Int,
    val type: String
) : Parcelable

data class ProductResponse(
    val products: List<Product>
)

//data class User(
//    val USERID: String,
//    val USERNAME: String,
//    val EMAIL: String,
//    val PASSWORD: String,
//    val PROFILEIMG: String,
//    val CREATEDAT: Timestamp,
//    val UPDATEDAT: Timestamp,
//    val skinId: String
//)

data class ApiResponse(
    val status: Boolean,
    val message: String,
    val data: List<User>
)

data class User(
    val USERID: String?,
    val USERNAME: String?,
    val EMAIL: String?,
    val PASSWORD: String?,
    val PROFILEIMG: String?,
    val CREATEDAT: String?,
    val UPDATEDAT: String?,
    val skinId: String?
)
