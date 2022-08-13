package com.prox.music_download6_ms3.music

import com.prox.music_download6_ms3.model.Music

enum class RepeatMode {LoopOne, LoopList, NoLoop}

object ManagerMusic {
    private var listMusic: List<Music> = ArrayList()
    private var currentMusic: Music? = null
    private var repeatMode = RepeatMode.NoLoop
    var isRandom = false

    fun setListMusic(musics: List<Music>){
        listMusic = musics
    }

    fun setCurrentMusic(music: Music?){
        currentMusic = music
    }

    fun getCurrentMusic(): Music?{
        return currentMusic
    }

    fun getIndexOfCurrentMusic(): Int {
        return listMusic.indexOf(currentMusic)
    }

    fun getSizeMusicList(): Int{
        return listMusic.size
    }

    fun nextMusic(){
        val pos = getIndexOfCurrentMusic()
        currentMusic = if (pos == listMusic.size - 1){
            listMusic[0]
        }else{
            listMusic[pos + 1]
        }
    }

    fun previousMusic(){
        val pos = getIndexOfCurrentMusic()
        currentMusic = if (pos == 0){
            listMusic[listMusic.size - 1]
        }else{
            listMusic[pos - 1]
        }
    }

    fun setRepeatStatus(repeatMode: RepeatMode){
        this.repeatMode = repeatMode
    }

    fun getRepeatStatus(): RepeatMode = repeatMode

    fun randomMusic(){
        val pos = (listMusic.indices).random()
        if(pos != getIndexOfCurrentMusic()){
            currentMusic = listMusic[pos]
        }else{
            randomMusic()
        }
    }
}