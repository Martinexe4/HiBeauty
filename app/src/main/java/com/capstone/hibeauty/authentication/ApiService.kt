package com.capstone.hibeauty.authentication

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
    fun getAllProducts(@Header("Authorization") token: String): Call<List<Product>>

    @GET("users")
    fun getUserProfile(@Header("Authorization") token: String): Call<User>


}