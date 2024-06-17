package com.capstone.hibeauty.api

import okhttp3.MultipartBody
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

    @GET("user/{USERID}/profile")
    fun getUserProfile2(@Path("USERID") userId: String): Call<UserProfileResponse>

    @GET("user/{userId}/profile")
    fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Call<UserProfileResponse>


}