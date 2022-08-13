package com.prox.music_download6_ms3.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_music_favorite")
data class Music(
    val albumId: String = "",
    val albumImage: String,
    val albumName: String,
    val artistId: String,
    val artistIdstr: String,
    val artistName: String,
    val audio: String,
    val audioDownload: String,
    val audioDownloadAllowed: Boolean,
    val duration: Int,
    @PrimaryKey
    val id: String,
    val image: String,
    val licenseCcurl: String,
    val name: String,
    val position: Int,
    val prourl: String,
    val releaseDate: String,
    val shareurl: String,
    val shorturl: String,
    val source: String,
    var isFavorite: Boolean = false,
    var isAddToPlaylist: Boolean = false
) : Serializable
