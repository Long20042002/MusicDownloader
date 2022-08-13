package com.prox.music_download6_ms3.networking

import com.prox.music_download6_ms3.networking.Key_api.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }

}