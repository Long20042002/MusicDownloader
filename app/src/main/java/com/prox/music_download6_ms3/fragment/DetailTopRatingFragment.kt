package com.prox.music_download6_ms3.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.adpater.TopListenedAdapter
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.FragmentDetailTopRatingBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.PlayMusicService
import com.prox.music_download6_ms3.ui.home.HomeViewModel

class DetailTopRatingFragment: BaseFragment(), MusicClick {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val binding = FragmentDetailTopRatingBinding.inflate(inflater, container , false)

        val adpaterListened = TopListenedAdapter(this)
        binding.rcvDetailTopRating.adapter = adpaterListened
        binding.rcvDetailTopRating.layoutManager = LinearLayoutManager(requireActivity())

        homeViewModel.general.observe(viewLifecycleOwner){
            adpaterListened.mList = it.data as ArrayList<Music>
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