package com.prox.music_download6_ms3.music

import android.content.Context
import com.prox.music_download6_ms3.model.MusicDownloading
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration

object ManagerMusicDownLoading {
    private val musicDownloadings = ArrayList<MusicDownloading>()

    var fetch: Fetch? = null

    fun listDownloading(): ArrayList<MusicDownloading> = musicDownloadings

    fun getFetch(context: Context): Fetch{
        if (fetch == null){
            val fetchConfiguration: FetchConfiguration = FetchConfiguration.Builder(context)
                .setDownloadConcurrentLimit(3)
                .build()

            fetch = Fetch.getInstance(fetchConfiguration)
        }
        return fetch!!
    }

}