package com.prox.music_download6_ms3.model

import android.graphics.Bitmap
import java.io.Serializable

data class MusicDownloaded(
    val music: String?,
    val artist: String?,
    val uri: String?,
    val bitmap: Bitmap?,
    val duration: String?
): Serializable
