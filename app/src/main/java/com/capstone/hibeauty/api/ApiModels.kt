package com.capstone.hibeauty.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//khusus
import com.google.gson.annotations.SerializedName

data class YourResponseModel(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UserData
)

data class UserData(
    @SerializedName("USERID")
    val userId: String,
    @SerializedName("USERNAME")
    val username: String,
    @SerializedName("EMAIL")
    val email: String,
    @SerializedName("PASSWORD")
    val password: String,
    @SerializedName("PROFILEIMG")
    val profileImage: String?, // Assuming profile image is a string path or URL
    @SerializedName("CREATEDAT")
    val createdAt: String,
    @SerializedName("UPDATEDAT")
    val updatedAt: String,
    @SerializedName("skinId")
    val skinId: String?, // Assuming skinId can be nullable
    @SerializedName("AGE")
    val age: Int,
    @SerializedName("GENDER")
    val gender: String
)
//khusus
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

data class ApiResponse(
    val status: Boolean,
    val message: String,
    val data: User
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



data class Recommendation(
    val name: String,
    val description: String // Adjust fields as per actual response
)

