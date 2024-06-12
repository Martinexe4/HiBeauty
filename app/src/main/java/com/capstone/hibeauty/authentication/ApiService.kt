package com.capstone.hibeauty.authentication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class RegisterRequest(val username: String, val email: String, val password: String, val confirmpassword: String)
data class LoginRequest(val email: String, val password: String)
data class ApiResponse(val message: String)

interface ApiService {

    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<ApiResponse>

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<ApiResponse>
}