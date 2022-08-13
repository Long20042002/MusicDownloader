package com.prox.music_download6_ms3.callback

import com.prox.music_download6_ms3.model.Music

interface ItemMusicPlayListClick {
    fun itemMusicPLClick(music: Music)
    fun dotClick(music: Music)
}