package com.prox.music_download6_ms3.model

data class GeneralSC<T: Any>(
    val status: String?,
    val currentOffset: Int?,
    val data: T
)
