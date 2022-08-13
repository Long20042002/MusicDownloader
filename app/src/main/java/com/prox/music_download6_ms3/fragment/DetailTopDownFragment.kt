package com.prox.music_download6_ms3.fragment

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
import com.prox.music_download6_ms3.databinding.FragmentDetailGenresBinding
import com.prox.music_download6_ms3.databinding.FragmentDetailTopDownBinding
import com.prox.music_download6_ms3.databinding.FragmentDownloadBinding
import com.prox.music_download6_ms3.model.Genres
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.PlayMusicService
import com.prox.music_download6_ms3.ui.home.HomeViewModel

class DetailTopDownFragment: BaseFragment(), MusicClick {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val binding = FragmentDetailTopDownBinding.inflate(inflater, container , false)

        val adpaterListened = TopListenedAdapter(this)
        binding.rcvDetailTopDown.adapter = adpaterListened
        binding.rcvDetailTopDown.layoutManager = LinearLayoutManager(requireActivity())

        homeViewModel.generalTopDownload.observe(viewLifecycleOwner){
            adpaterListened.mList = it.data as ArrayList<Music>
            adpaterListened.notifyDataSetChanged()
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