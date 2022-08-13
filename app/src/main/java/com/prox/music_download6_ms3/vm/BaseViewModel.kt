package com.prox.music_download6_ms3.vm

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.model.MusicDownloading
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.ManagerMusicDownLoading
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BaseViewModel(application: Application) : AndroidViewModel(application) {

    fun startDownload(music: Music, path: String) {
        val request = Request(music.audioDownload, path)
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL

        val musicDownloading = MusicDownloading(
            music.name,
            music.artistName,
            music.image,
            request
        )

        ManagerMusicDownLoading.listDownloading().add(musicDownloading)

        viewModelScope.launch(Dispatchers.IO) {
            ManagerMusicDownLoading.getFetch(getApplication()).enqueue(
                request,
                { Toast.makeText(getApplication(), "Downloading...", Toast.LENGTH_SHORT).show() },
                { error ->
                    Toast.makeText(getApplication(), "Error Download!...", Toast.LENGTH_SHORT)
                        .show()
                })

            ManagerMusicDownLoading.fetch!!.addListener(
                object : FetchListener {
                    override fun onAdded(download: Download) {}
                    override fun onCancelled(download: Download) {}
                    override fun onCompleted(download: Download) {
                        ManagerMusicDownLoading.listDownloading().remove(musicDownloading)
                        ManagerMusicDownLoaded.getMusicFromFile()
                    }

                    override fun onDeleted(download: Download) {}
                    override fun onDownloadBlockUpdated(
                        download: Download,
                        downloadBlock: DownloadBlock,
                        totalBlocks: Int
                    ) {
                    }

                    override fun onError(download: Download, error: Error, throwable: Throwable?) {
                        ManagerMusicDownLoading.fetch!!.retry(request.id)
                    }

                    override fun onPaused(download: Download) {}

                    override fun onProgress(
                        download: Download,
                        etaInMilliSeconds: Long,
                        downloadedBytesPerSecond: Long
                    ) {
                    }

                    override fun onQueued(download: Download, waitingOnNetwork: Boolean) {}
                    override fun onRemoved(download: Download) {}
                    override fun onResumed(download: Download) {}
                    override fun onStarted(
                        download: Download,
                        downloadBlocks: List<DownloadBlock>,
                        totalBlocks: Int
                    ) {
                    }

                    override fun onWaitingNetwork(download: Download) {}
                }
            )
        }
    }
}