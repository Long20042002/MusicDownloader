package com.prox.music_download6_ms3.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.adpater.DetailGenresAdatper
import com.prox.music_download6_ms3.adpater.GenresAdapter
import com.prox.music_download6_ms3.callback.GenresClick
import com.prox.music_download6_ms3.databinding.FragmentDetailGenresBinding
import com.prox.music_download6_ms3.model.Genres
import com.prox.music_download6_ms3.ui.home.HomeFragmentDirections
import com.prox.music_download6_ms3.ui.home.HomeViewModel

class DetailGenresFragment : Fragment(), GenresClick {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val binding = FragmentDetailGenresBinding.inflate(inflater, container, false)

        val detailGenresAdapter = DetailGenresAdatper(this)
        binding.rcvDetailGenres.adapter = detailGenresAdapter
        binding.rcvDetailGenres.layoutManager =
            GridLayoutManager(requireActivity(), 3, RecyclerView.VERTICAL, false)

        homeViewModel.generalGenres.observe(viewLifecycleOwner) {
            detailGenresAdapter.mList = it.data as ArrayList<Genres>
            detailGenresAdapter.notifyDataSetChanged()
        }

        binding.ivBackHome.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
        return binding.root
    }

    override fun itemGenresClick(genres: Genres) {
        Log.d("TAG", "itemGenresClick: ")
        val action =
            DetailGenresFragmentDirections.actionDetailGenresFragmentToMusicFromGenres(genres)
        requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
    }
}