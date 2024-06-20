package com.capstone.hibeauty.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("products")
    fun getAllProducts(@Header("Authorization") token: String): Call<ProductResponse>

    @POST("user/{id}/age-gender")
    fun updateUserAgeGender(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Body ageGenderRequest: AgeGenderRequest
    ): Call<AgeGenderResponse>

    @GET("user/{userId}/profile")
    fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Call<UserProfileResponse>

    @GET("recommendations/{skinId}")
    fun getRecommendations(
        @Path("skinId") skinId: String,
        @Header("Authorization") token: String
    ): Call<RecommendationResponse>


    @GET("user/{userId}/profile-image")
    fun getUserProfileImage(
        @Header("Authorization") authToken: String,
        @Path("userId") userId: String
    ): Call<ProfileImageResponse>

    @Multipart
    @POST("skin/upload")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Call<JSONObject>

    @POST("predictions")
    fun savePrediction(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Call<JSONObject>

    @Multipart
    @PUT("user/{id}")
    fun uploadProfileImage(
        @Header("Authorization") authHeader: String,
        @Path("id") userId: String,
        @Part image: MultipartBody.Part
    ): Call<ResponseBody>

    @GET ("everything?q=wajah&language=id&sortBy=popularity&apiKey=edf3226670f340bea5c2ecac0db6e19d")
    fun getNews(): Call<NewsResponse>

}