package com.redy.cpv2.article

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServiceNews {
    @GET ("everything?q=wajah&language=id&sortBy=popularity&apiKey=edf3226670f340bea5c2ecac0db6e19d")
    fun getNews(): Call<NewsResponse>
}
