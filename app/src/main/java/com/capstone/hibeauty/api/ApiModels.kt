package com.capstone.hibeauty.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//khusus
import com.google.gson.annotations.SerializedName
import java.io.Serializable

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

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val ingridients: String,
    val link: String,
    val image: String,
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

data class ProductRecommendation(
    val id: Int,
    val name: String,
    val description: String,
    val ingridients: String,
    val link: String,
    val image: String,
    val recomId: Int,
    val typeId: Int
)

data class RecommendationResponse(
    val status: Boolean,
    val message: String,
    val data: List<ProductRecommendation>
)


//load photo profile
data class ProfileImageResponse(
    val status: Boolean,
    val message: String,
    val data: ProfileImageData
)

data class ProfileImageData(
    val profileImage: String?
)


//news
data class NewsResponse(

    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("articles")
    val articles: List<ArticlesItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class ArticlesItem(

    @field:SerializedName("publishedAt")
    val publishedAt: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("urlToImage")
    val urlToImage: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("source")
    val source: Source? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("content")
    val content: String? = null
): Serializable

data class Source(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Any? = null
): Serializable
