package com.prox.music_download6_ms3.model

import com.tonyodev.fetch2.Request

data class MusicDownloading(
    val music: String,
    val artist: String,
    val urlImage: String,
    val request: Request
)
