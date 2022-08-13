package com.prox.music_download6_ms3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prox.music_download6_ms3.music.PlayMusicService

class MediaReveiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionMusic = intent!!.getIntExtra("action_music", 0)

        val intentService = Intent(context, PlayMusicService::class.java)
        intentService.putExtra("action_music_service", actionMusic)

        context!!.startService(intentService)
    }
}