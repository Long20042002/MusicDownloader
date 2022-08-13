package com.prox.music_download6_ms3

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.prox.music_download6_ms3.sharedPreferences.ManagerShared
import com.prox.music_download6_ms3.utils.Utils

class App: Application() {

    companion object{
        const val CHANNEL_ID = "CHANNEL_MUSIC_APP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Utils.initPath(this)
        ManagerShared.init(applicationContext)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Channel music",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(null, null)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(0)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}