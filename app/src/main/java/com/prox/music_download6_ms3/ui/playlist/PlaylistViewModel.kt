package com.prox.music_download6_ms3.ui.playlist

import android.app.Application
import androidx.lifecycle.*
import com.prox.music_download6_ms3.db.MusicDatabase
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.model.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(application: Application) : AndroidViewModel(application) {

    private var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var _musicPL = MutableLiveData<PlayList>()
    val musicPL: LiveData<PlayList> = _musicPL

    private var db = MusicDatabase.getInstance(application)
    private var playListDAO = db.PlayListDAO()
    private var musicFavoriteDAO = db.MusicDao()

    val playLists = playListDAO.getAllPlayList()
    val musics = musicFavoriteDAO.getAllMusicFavorite()

    //PlayList
    fun addPL(playList: PlayList){
        viewModelScope.launch {
            playListDAO.insertPL(playList)
        }
    }

    fun deletePL(playList: PlayList){
        viewModelScope.launch {
            playListDAO.deletePL(playList)
        }
    }

    fun updatePL(playList: PlayList){
        viewModelScope.launch {
            playListDAO.updatePL(playList)
        }
    }

    fun updateMusicPL(playList: PlayList, music: Music){
        viewModelScope.launch {
           if (!isMusicEx(music, playList.listMusic)){
               playList.listMusic.add(music)
               playListDAO.updatePL(playList)
           }else{
               return@launch
           }
        }
    }


    fun getPlayListByID(id: Int){
        viewModelScope.launch {
            _musicPL.postValue(playListDAO.getPlaylistFromID(id))
        }
    }

    private fun isMusicEx(music: Music, musicList: List<Music>): Boolean{
        for(i in musicList.indices){
            if(music.id == musicList[i].id){
                return true
            }
        }
        return false
    }

    //Music Favorite
    fun addFR(music: Music){
        viewModelScope.launch {
            musicFavoriteDAO.insertMS(music)
        }
    }

    fun deleteFR(music: Music){
        viewModelScope.launch {
            musicFavoriteDAO.deleteMS(music)
        }
    }



    fun checkMusicFavorite(id: String): Music?{
        return musicFavoriteDAO.getMusic(id)
    }
}