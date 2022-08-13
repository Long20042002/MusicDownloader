package com.prox.music_download6_ms3.music

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.prox.music_download6_ms3.App.Companion.CHANNEL_ID
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.MediaReveiver
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.model.*
import com.prox.music_download6_ms3.networking.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class PlayMusicService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    companion object {
        const val ACTION_START = 1
        const val ACTION_PAUSE = 2
        const val ACTION_RESUME = 3
        const val ACTION_NEXT = 4
        const val ACTION_PREVIOUS = 5
        const val ACTION_CLOSE = 6
    }

    var mediaPlayer: MediaPlayer? = MediaPlayer()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle: Bundle? = intent?.extras

        val actionMusic: Int? = intent?.getIntExtra("action_music_service", 0)
        if (actionMusic != null) {
            Log.d("TAG", "onStartCommand: $actionMusic")
            handleActionMusic(actionMusic)
        }
        return START_NOT_STICKY
    }

    private fun sendNotificationMedia(music: Music? = null) {
        val notifyIntent = Intent(this, MainActivity::class.java)
        notifyIntent.putExtra("openFromNoti", "notiOP")
        notifyIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val notifyPendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        scope.launch {
            val builder = NotificationCompat.Builder(this@PlayMusicService, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_icon_download)
                .setContentIntent(notifyPendingIntent)
                .addAction(
                    R.drawable.ic_previous_noti,
                    "Previous",
                    getPendingIntent(this@PlayMusicService, ACTION_PREVIOUS)
                )
                .addAction(
                    if (ManagerMedia.isPlaying) R.drawable.ic_pause_noti else R.drawable.ic_play_noti,
                    if (ManagerMedia.isPlaying) "Pause" else "Resume",
                    if (ManagerMedia.isPlaying) getPendingIntent(
                        this@PlayMusicService,
                        ACTION_PAUSE
                    ) else getPendingIntent(this@PlayMusicService, ACTION_RESUME)
                )
                .addAction(
                    R.drawable.ic_next_noti,
                    "Next",
                    getPendingIntent(this@PlayMusicService, ACTION_NEXT)
                )
                .addAction(
                    R.drawable.ic_clear,
                    "Clear",
                    getPendingIntent(this@PlayMusicService, ACTION_CLOSE)
                )
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2 /* #1: pause button */)
                )

            if (music == null) {
                builder.setContentTitle(ManagerMusicDownLoaded.currentMusicDownloaded!!.music)
                    .setContentText(ManagerMusicDownLoaded.currentMusicDownloaded!!.artist)
                    .setLargeIcon(ManagerMusicDownLoaded.currentMusicDownloaded!!.bitmap)
            } else {
                builder.setContentTitle(music.name)
                    .setContentText(music.artistName)
                    .setLargeIcon(getBitmapFromURL(music.image))
            }

            val notification = builder.build()
            startForeground(1, notification)
        }
    }

    private fun startMusic(urlMS: String) {
        ManagerMedia.playMusic(urlMS) {
            if (ManagerMusic.isRandom) {
                ManagerMusic.randomMusic()
                EventBus.getDefault().post(MesFromEventBus(ACTION_NEXT))
            } else {
                when (ManagerMusic.getRepeatStatus()) {
                    RepeatMode.NoLoop -> {
                        if (ManagerMusic.getIndexOfCurrentMusic() == ManagerMusic.getSizeMusicList() - 1) {
                            EventBus.getDefault().post(MesFromEventBus(ACTION_PAUSE))
                            ManagerMedia.pauseMedia()
                        } else {
                            ManagerMusic.nextMusic()
                            EventBus.getDefault().post(MesFromEventBus(ACTION_NEXT))
                        }
                    }
                    RepeatMode.LoopList -> {
                        ManagerMusic.nextMusic()
                        EventBus.getDefault().post(MesFromEventBus(ACTION_NEXT))
                    }
                    RepeatMode.LoopOne -> {
                        EventBus.getDefault().post(MesFromEventBus(ACTION_NEXT))
                    }
                }
                ManagerMusic.getCurrentMusic()?.let { it1 -> sendNotificationMedia(it1) }
            }
        }
    }

    private fun startMusicDownloaded(musicDownloaded: MusicDownloaded) {
        ManagerMedia.playMusic(musicDownloaded.uri.toString()) {
            when (ManagerMusic.getRepeatStatus()) {
                RepeatMode.NoLoop -> {
                    if (ManagerMusicDownLoaded.getIndexOfCurrentMusic() == ManagerMusicDownLoaded.getSizeMusicList() - 1) {
                        EventBus.getDefault().post(MesFromEventBus(ACTION_PAUSE))
                        ManagerMedia.pauseMedia()
                    } else {
                        ManagerMusicDownLoaded.nextMusic()
                        EventBus.getDefault().post(MesFromEventBus(ACTION_NEXT))
                    }
                }
                RepeatMode.LoopList -> {
                    ManagerMusicDownLoaded.nextMusic()
                    EventBus.getDefault().post(MesFromEventBus(ACTION_NEXT))
                }
                RepeatMode.LoopOne -> {
                    EventBus.getDefault().post(MesFromEventBus(ACTION_NEXT))
                }
            }
            sendNotificationMedia()
        }
    }

    private fun getMusicFromSC(music: Music) {
        if (music.source.equals("SC")) {
            RetrofitInstance.api.getMusicSC(music.id).enqueue(object : Callback<GeneralSC<String>> {
                override fun onResponse(
                    call: Call<GeneralSC<String>>,
                    response: Response<GeneralSC<String>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { startMusic(it) }
                    }
                }

                override fun onFailure(call: Call<GeneralSC<String>>, t: Throwable) {}
            })
        } else {
            startMusic(music.audio!!)
        }
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent? {
        val intent = Intent(this, MediaReveiver::class.java)
        intent.putExtra("action_music", action)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private suspend fun getBitmapFromURL(url: String): Bitmap? {
        val loader = coil.ImageLoader(this@PlayMusicService)
        val request = ImageRequest.Builder(this@PlayMusicService)
            .data(url)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            ACTION_PAUSE -> {
                ManagerMedia.pauseMedia()
            }
            ACTION_RESUME -> {
                ManagerMedia.resumeMedia()
            }
            ACTION_CLOSE -> {
                stopSelf()
            }
            ACTION_PREVIOUS -> {
                if (ManagerMusicDownLoaded.currentMusicDownloaded == null) {
                    ManagerMusic.previousMusic()
                } else {
                    ManagerMusicDownLoaded.previousMusic()
                }

//                ManagerMusic.previousMusic()
//                ManagerMusic.getCurrentMusic()?.let { startMusic(it.audio) }
            }
            ACTION_NEXT -> {
                if (ManagerMusic.isRandom) {
                    if (ManagerMusicDownLoaded.currentMusicDownloaded == null) {
                        ManagerMusic.randomMusic()
                    } else {
                        ManagerMusicDownLoaded.randomMusic()
                    }
                } else {
                    if (ManagerMusicDownLoaded.currentMusicDownloaded == null) {
                        ManagerMusic.nextMusic()
                    } else {
                        ManagerMusicDownLoaded.nextMusic()
                    }
                }
            }
            ACTION_START -> {
                if (ManagerMusicDownLoaded.currentMusicDownloaded == null) {
                    ManagerMusic.getCurrentMusic()?.let { getMusicFromSC(it) }
                } else {
                    ManagerMusicDownLoaded.currentMusicDownloaded?.let { startMusicDownloaded(it) }
                }
            }
        }
        EventBus.getDefault().post(MesFromEventBus(action))
        if (ManagerMusicDownLoaded.currentMusicDownloaded == null) {
            ManagerMusic.getCurrentMusic()?.let { sendNotificationMedia(it) }
        } else {
            sendNotificationMedia()
        }
//        sendNotificationMedia(ManagerMusic.getCurrentMusic())

    }


    override fun onDestroy() {
        super.onDestroy()
        ManagerMedia.isStop = true;
        ManagerMedia.mediaPlayer!!.stop()
        job.cancel()
    }
}