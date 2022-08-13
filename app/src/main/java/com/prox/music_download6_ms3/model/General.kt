package com.prox.music_download6_ms3.model

data class General(
    val currentOffset: Int,
    val `data`: List<Music>,
    val status: String
)
