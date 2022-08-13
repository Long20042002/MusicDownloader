package com.prox.music_download6_ms3.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "table_playlist")
data class PlayList(
    var name: String,
    val listMusic: ArrayList<Music>,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Serializable
