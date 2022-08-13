package com.prox.music_download6_ms3.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.adpater.TopListenedAdapter
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.FragmentDetailTopRatingBinding
import com.prox.music_download6_ms3.databinding.FragmentMusicFavoriteBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.PlayMusicService
import com.prox.music_download6_ms3.ui.playlist.PlaylistViewModel

class MusicFavoriteFragment: BaseFragment(), MusicClick {

    private lateinit var playlistViewModel : PlaylistViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        playlistViewModel = ViewModelProvider(requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[PlaylistViewModel::class.java]

        val binding = FragmentMusicFavoriteBinding.inflate(inflater, container , false)

        val adpaterListened = TopListenedAdapter(this)
        binding.rcvMusicFavorite.adapter = adpaterListened
        binding.rcvMusicFavorite.layoutManager = LinearLayoutManager(requireActivity())

        playlistViewModel.musics.observe(viewLifecycleOwner){
            adpaterListened.mList = it as ArrayList<Music>
            adpaterListened.notifyDataSetChanged()
        }

        binding.ivBackHome.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return binding.root
    }

    override fun clickPlayMusic(music: Music) {
        ManagerMusic.setCurrentMusic(music)
        ManagerMusicDownLoaded.currentMusicDownloaded = null
        sendActionToService(PlayMusicService.ACTION_START)
    }

}