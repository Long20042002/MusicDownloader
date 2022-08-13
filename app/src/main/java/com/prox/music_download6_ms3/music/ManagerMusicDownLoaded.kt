package com.prox.music_download6_ms3.music

import android.graphics.BitmapFactory
import com.prox.music_download6_ms3.model.MusicDownloaded
import com.prox.music_download6_ms3.utils.Utils
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File

object ManagerMusicDownLoaded {

    var musicDowloadeds: ArrayList<MusicDownloaded> = ArrayList()
    var currentMusicDownloaded: MusicDownloaded? = null

    fun getMusicFromFile() {
        val file = File(Utils.PATH)
        musicDowloadeds.clear()
        val mediaMetadataRetriever = FFmpegMediaMetadataRetriever()
        file.listFiles()?.forEach { musicFile ->
            if (musicFile.length() != 0L && !comparePaths(musicFile.path)) {
                try {
                    mediaMetadataRetriever.setDataSource(musicFile.path)

                    val songName =
                        mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE)

                    val artist =
                        mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST)

                    val art =
                        mediaMetadataRetriever.embeddedPicture

                    val duration =
                        mediaMetadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)

                    val songImage = art?.let {
                        BitmapFactory.decodeByteArray(art, 0, it.size)
                    }

                    if (duration.length > 1) {
                        musicDowloadeds.add(
                            MusicDownloaded(
                                songName,
                                artist,
                                musicFile.path,
                                songImage,
                                duration.substring(0, 3)
                            )
                        )
                    } else {
                        musicDowloadeds.add(
                            MusicDownloaded(
                                songName, artist, musicFile.path, songImage, duration
                            )
                        )
                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }

        }
    }


    private fun comparePaths(path: String): Boolean {
        if (ManagerMusicDownLoading.listDownloading().isEmpty()) {
            return false
        } else {
            ManagerMusicDownLoading.listDownloading().forEach {
                if (path == it.request.file) {
                    return true
                }
            }
        }
        return false
    }

    fun getIndexOfCurrentMusic(): Int {
        return musicDowloadeds.indexOf(currentMusicDownloaded)
    }

    fun getSizeMusicList(): Int {
        return musicDowloadeds.size
    }


    fun nextMusic() {
        val pos = getIndexOfCurrentMusic()
        currentMusicDownloaded = if (pos == musicDowloadeds.size - 1) {
            musicDowloadeds[0]
        } else {
            musicDowloadeds[pos + 1]
        }
    }

    fun previousMusic() {
        val pos = getIndexOfCurrentMusic()
        currentMusicDownloaded = if (pos == 0) {
            musicDowloadeds[musicDowloadeds.size - 1]
        } else {
            musicDowloadeds[pos - 1]
        }
    }

    fun randomMusic() {
        val pos = (musicDowloadeds.indices).random()
        if (pos != getIndexOfCurrentMusic()) {
            currentMusicDownloaded = musicDowloadeds[pos]
        } else {
            randomMusic()
        }
    }
}