package com.prox.music_download6_ms3.music

import android.media.AudioManager
import android.media.MediaPlayer
import java.io.IOException

object ManagerMedia {
    var mediaPlayer: MediaPlayer? = MediaPlayer()
    var isPlaying = false
    var isStop = true

    fun playMusic(url: String, completionListener: MediaPlayer.OnCompletionListener ) {
        isPlaying = true
        isStop = false
        mediaPlayer?.reset()
        try {

            mediaPlayer?.setDataSource(url)
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer?.prepareAsync()

            mediaPlayer?.setOnCompletionListener(completionListener)

        }catch (e: IllegalArgumentException){
            e.printStackTrace()
        } catch (e: IllegalStateException){
            e.printStackTrace()
        }catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun pauseMedia() {
        if (mediaPlayer != null) {
            isPlaying = false
            mediaPlayer!!.pause()
        }
    }

    fun resumeMedia() {
        if (mediaPlayer != null) {
            isPlaying = true
            mediaPlayer!!.start()
        }
    }

    fun getProgress(): Int {
        return if (mediaPlayer != null) {
            mediaPlayer!!.currentPosition
        } else 0
    }

    fun setProgress(milli: Int) {
        if (mediaPlayer != null) {
            mediaPlayer!!.seekTo(milli)
        }
    }
}