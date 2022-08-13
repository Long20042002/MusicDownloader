package com.prox.music_download6_ms3.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prox.music_download6_ms3.model.Music

class ConverterListMusic {

    @androidx.room.TypeConverter
    fun fromMusic(music: Music): String?{
        return Gson().toJson(music)
    }

    @androidx.room.TypeConverter
    fun toMusic(music: String): Music?{
        return Gson().fromJson(music, Music::class.java)
    }
    @androidx.room.TypeConverter
    fun fromListMusic(list: ArrayList<Music>?): String?{
        val listType = object : TypeToken<ArrayList<Music>>() {}.type
        return Gson().toJson(list, listType)

    }

    @androidx.room.TypeConverter
    fun toListMusic(list: String): ArrayList<Music>? {
        val listType = object : TypeToken<ArrayList<Music>>() {}.type
        return Gson().fromJson<ArrayList<Music>>(list, listType)

    }
}