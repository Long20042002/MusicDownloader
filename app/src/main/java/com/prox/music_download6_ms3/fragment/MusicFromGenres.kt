package com.prox.music_download6_ms3.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.style.Wave
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.adpater.TopListenedAdapter
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.FragmentMusicFromGenresBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.PlayMusicService
import com.prox.music_download6_ms3.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MusicFromGenres : BaseFragment(), MusicClick {

    lateinit var homeViewModel: HomeViewModel
    private val args: MusicFromGenresArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val binding = FragmentMusicFromGenresBinding.inflate(inflater, container, false)

        val adpaterListened = TopListenedAdapter(this)
        binding.rcvMusicGenres.adapter = adpaterListened
        binding.rcvMusicGenres.layoutManager = LinearLayoutManager(requireActivity())

        binding.tvNameGenres.text = args.genres.name
        loadMusicByGenres()

        homeViewModel.generalByGenres.observe(viewLifecycleOwner) {
            if (it == null){
                adpaterListened.notifyDataSetChanged()
            }else {
                adpaterListened.mList = it.data as ArrayList<Music>
                adpaterListened.notifyDataSetChanged()
            }
        }

        binding.ivBackHome.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        binding.progressBarCustom.indeterminateDrawable = Wave()

        return binding.root
    }

    override fun clickPlayMusic(music: Music) {
        ManagerMusic.setCurrentMusic(music)
        ManagerMusicDownLoaded.currentMusicDownloaded = null
        sendActionToService(PlayMusicService.ACTION_START)
    }


    private fun loadMusicByGenres() {
        args.genres.keySearch?.let { homeViewModel.getMusicByGenres(it, "vn", 25, false) }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeViewModel.getMusicByGenres("","vn", 25, true)
    }
}