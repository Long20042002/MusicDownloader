package com.prox.music_download6_ms3.networking

import com.prox.music_download6_ms3.model.General
import com.prox.music_download6_ms3.model.GeneralGenres
import com.prox.music_download6_ms3.model.GeneralSC
import com.prox.music_download6_ms3.model.Music
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("{option}?")
    suspend fun getMusics(
        @Path("option") option: String,
        @Query("offset") offset: Int,
        @Query("country") country: String = "vn"): General

    @GET("url?")
    fun getMusicSC(@Query("id") id : String) : Call<GeneralSC<String>>

    @GET("genre")
    suspend fun getGenres(): GeneralGenres

    @GET("search?")
    suspend fun searchMusic(
        @Query("offset") offset: Int,
        @Query("query") country: String = ""): General

    @GET("bygenre?")
    suspend fun getMusicByGenres(
        @Query("query") genres: String,
        @Query("offset") offset: Int,
        @Query("country") country: String = "vn") : General

}