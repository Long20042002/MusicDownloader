package com.prox.music_download6_ms3.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.adpater.TopListenedAdapter
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.FragmentSearchBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.PlayMusicService
import com.prox.music_download6_ms3.ui.home.HomeViewModel

class SearchFragment:BaseFragment(), MusicClick {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding = FragmentSearchBinding.inflate(inflater, container, false)


        setTextColorSearch()
        binding.ivBackHome.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        val adapter = TopListenedAdapter(this)
        binding.rcvSearch.adapter = adapter
        binding.rcvSearch.layoutManager = LinearLayoutManager(requireActivity())

        homeViewModel.searchGeneral.observe(viewLifecycleOwner){
            adapter.mList = it.data as ArrayList<Music>
            adapter.notifyDataSetChanged()
        }

        binding.searchSong.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.searchSong(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        return binding.root
    }

    private fun setTextColorSearch() {
        val theTextArea = binding.searchSong.findViewById<View>(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        theTextArea.setTextColor(Color.parseColor("#888888"))
        theTextArea.setHintTextColor(Color.parseColor("#888888"))
    }

    override fun clickPlayMusic(music: Music) {
        ManagerMusic.setCurrentMusic(music)
        ManagerMusicDownLoaded.currentMusicDownloaded = null
        sendActionToService(PlayMusicService.ACTION_START)
    }
}