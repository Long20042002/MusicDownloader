package com.prox.music_download6_ms3.callback

import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.model.MusicDownloaded

interface MusicDownloadClick {
    fun clickPlayMusic(musicDownloaded: MusicDownloaded)
    fun menuClick(musicDownloaded: MusicDownloaded)

}