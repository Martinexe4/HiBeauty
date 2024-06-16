package com.capstone.hibeauty.api

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
    val status: Boolean,
    val message: String,
    val data: User?,
    val token: String?
)

data class User(
    val USERID: String,
    val USERNAME: String,
    val EMAIL: String,
    val PASSWORD: String,
    val PROFILEIMG: String?,
    val CREATEDAT: String,
    val UPDATEDAT: String,
    val skinId: String?,
    val AGE: Int?,
    val GENDER: String?
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val userid: String?
)

data class AgeGenderRequest(
    val AGE: Int,
    val GENDER: String
)

data class AgeGenderResponse(
    val status: Boolean,
    val message: String
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
    val status: Boolean,
    val message: String,
    val data: List<Product>
)

data class UserProfileResponse(
    val status: Boolean,
    val message: String,
    val data: UserProfileData
)

data class UserProfileData(
    val USERNAME: String,
    val AGE: Int,
    val GENDER: String
)

data class ApiResponse(
    val status: Boolean,
    val message: String,
    val data: List<User>
)


