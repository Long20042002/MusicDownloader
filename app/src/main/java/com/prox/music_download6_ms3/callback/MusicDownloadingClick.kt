package com.prox.music_download6_ms3.callback

import com.prox.music_download6_ms3.model.MusicDownloading

interface MusicDownloadingClick {
    fun pauseOrResumeClick(musicDownloading: MusicDownloading)
    fun menuMusicDownloadingClick(musicDownloading: MusicDownloading)
}