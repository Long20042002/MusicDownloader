package com.prox.music_download6_ms3.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prox.music_download6_ms3.model.General
import com.prox.music_download6_ms3.model.GeneralGenres
import com.prox.music_download6_ms3.model.Genres
import com.prox.music_download6_ms3.networking.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val general = MutableLiveData<General>()
    val generalTrending = MutableLiveData<General>()
    val generalTopListened = MutableLiveData<General>()
    val generalTopDownload = MutableLiveData<General>()
    val generalGenres = MutableLiveData<GeneralGenres>()
    val generalByGenres = MutableLiveData<General>()
    val searchGeneral = MutableLiveData<General>()

    val statusGeneraTrending = MutableLiveData<Boolean>()
    private val api = RetrofitInstance.api

    init {
        getMusics("download", 0, null)
        getMusicTreding("popular", 0, null)
        getMusicTopListened("listened", 0, null)
        getMusicTopDownload("download", 0, null)
        getAllGenres()
    }

    private fun getMusics(option: String, offset: Int, country: String?){
        if (country == null){
            viewModelScope.launch(Dispatchers.IO) {
                val res = api.getMusics(option, offset)
                general.postValue(res)
            }
        }
    }

    private fun getMusicTreding(option: String, offset: Int, country: String?){
        statusGeneraTrending.postValue(true)
        if (country == null){
            viewModelScope.launch(Dispatchers.IO) {
                val res = api.getMusics(option, offset)
                generalTrending.postValue(res)
                statusGeneraTrending.postValue(true)
            }
        }
    }

    private fun getMusicTopListened(option: String, offset: Int, country: String?){
        if (country == null){
            viewModelScope.launch(Dispatchers.IO) {
                val res = api.getMusics(option, offset)
                generalTopListened.postValue(res)
            }
        }
    }

    private fun getMusicTopDownload(option: String, offset: Int, country: String?){
        if (country == null){
            viewModelScope.launch(Dispatchers.IO) {
                val res = api.getMusics(option, offset)
                generalTopDownload.postValue(res)
            }
        }
    }

    private fun getAllGenres(){
        viewModelScope.launch (Dispatchers.IO){
            val res = api.getGenres()
            generalGenres.postValue(res)
        }
    }

    fun getMusicByGenres(genres: String, country: String, offset: Int, check : Boolean){
        if (check){
            generalByGenres.postValue(null)
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                val res = api.getMusicByGenres(genres, offset, country)
                generalByGenres.postValue(res)
            }
        }

    }

    fun searchSong(country: String?, offset: Int = 0){
        viewModelScope.launch {
            val res = country?.let { api.searchMusic(offset, it) }
            searchGeneral.postValue(res)
        }
    }

}