package com.prox.music_download6_ms3.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.PlayMusicService
import com.prox.music_download6_ms3.utils.Utils
import com.prox.music_download6_ms3.vm.BaseViewModel
import java.io.File

abstract class BaseFragment: Fragment(){
    protected lateinit var file: File
    protected lateinit var mViewModel: BaseViewModel
    protected lateinit var music: Music

    protected fun downloadMS(music: Music){
        this.music = music
        file = File(Utils.PATH)
        if (!file.exists()){
            file.mkdir()
        }

        if (music.audioDownload != null){
            adsWhenDownload()
        }
        else{
            val toast = Toast.makeText(context, "Can't download", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(requireActivity())[BaseViewModel(activity?.application!!)::class.java]

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun sendActionToService(action: Int) {
        val intent = Intent(requireActivity(), PlayMusicService::class.java)
        intent.putExtra("action_music_service", action)
        requireContext().startService(intent)
    }

    protected fun shareMusic(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        intent.putExtra(Intent.EXTRA_TEXT, ManagerMusic.getCurrentMusic()?.audio)
        startActivity(Intent.createChooser(intent, "Share link"))
    }

    private fun adsWhenDownload(){

        val path = if(music.name!!.contains("/")){
            file.toString() +"/" + music.name!!.split("/")[0].plus(".mp3")
        } else{
            file.toString() +"/"+ music.name.plus(".mp3")
        }

        if(File(path).exists()){
            Toast.makeText(context, "Can't download because it was downloaded or downloading", Toast.LENGTH_SHORT).show()
        }
        else{
            mViewModel.startDownload(music, path)
        }
    }
}