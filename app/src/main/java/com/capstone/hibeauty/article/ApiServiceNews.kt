package com.capstone.hibeauty.article

import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceNews {
    @GET ("everything?q=wajah&language=id&sortBy=popularity&apiKey=edf3226670f340bea5c2ecac0db6e19d")
    fun getNews(): Call<NewsResponse>
}
