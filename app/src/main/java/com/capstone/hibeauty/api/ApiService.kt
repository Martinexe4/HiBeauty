package com.capstone.hibeauty.api

import com.capstone.hibeauty.api.ApiResponse
import com.capstone.hibeauty.api.LoginRequest
import com.capstone.hibeauty.api.LoginResponse
import com.capstone.hibeauty.api.ProductResponse
import com.capstone.hibeauty.api.RegisterRequest
import com.capstone.hibeauty.api.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("products")
    fun getAllProducts(@Header("Authorization") token: String): Call<ProductResponse>

    @GET("users")
    fun getUserProfile(@Header("Authorization") token: String): Call<ApiResponse>


}