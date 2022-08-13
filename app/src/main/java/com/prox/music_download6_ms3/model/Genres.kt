package com.prox.music_download6_ms3.model

import java.io.Serializable

data class Genres(
    val id: String?,
    val createdAt: Long?,
    val name: String?,
    val keySearch: String?,
    var image: String?
):Serializable
