package com.prox.music_download6_ms3.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.github.ybq.android.spinkit.style.Wave
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.adpater.*
import com.prox.music_download6_ms3.callback.GenresClick
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.FragmentHomeBinding
import com.prox.music_download6_ms3.model.Genres
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.PlayMusicService
import kotlin.math.abs

class HomeFragment : Fragment(), MusicClick, GenresClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapterTopRating = TopRatingAdapter(this)
        binding.rcvTopRating.adapter = adapterTopRating
        binding.rcvTopRating.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        homeViewModel.general.observe(viewLifecycleOwner){
            adapterTopRating.mList = it.data as ArrayList<Music>
            adapterTopRating.notifyDataSetChanged()
        }

        val adapterTrending = TrendingAdpater(this,  binding.viewPagerTrending)
        homeViewModel.generalTrending.observe(viewLifecycleOwner){
            adapterTrending.mList = it.data as ArrayList<Music>
            adapterTrending.notifyDataSetChanged()
        }

        binding.viewPagerTrending.adapter = adapterTrending
        binding.viewPagerTrending.clipToPadding = false
        binding.viewPagerTrending.clipChildren = false
        binding.viewPagerTrending.offscreenPageLimit = 3
        binding.viewPagerTrending.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->

            val scale = 0.6f + (1 - 0.8f) * (1 - abs(position))
            if (position <= 0f) {
                page.translationX = page.width * -position * 0.20f
//                page.pivotY = 0.5f * page.height
//                page.pivotX = 0f * page.width
                page.scaleX = scale
                page.scaleY = scale
            } else if (position <= 2f) {
                page.translationX = page.width * -position * 0.20f
//                page.pivotY = 0.5f * page.height
//                page.pivotX = 0.6f * page.width
                page.scaleX = scale
                page.scaleY = scale
            }
        }

        binding.viewPagerTrending.setPageTransformer(compositePageTransformer)

        val adapterTopListened = TopListenedLimit5Adapter(this)
        binding.rcvTopListened.adapter = adapterTopListened
        binding.rcvTopListened.layoutManager = LinearLayoutManager(requireActivity())

        homeViewModel.generalTopListened.observe(viewLifecycleOwner){
            adapterTopListened.mList = it.data as ArrayList<Music>
            adapterTopListened.notifyDataSetChanged()
        }

        val adapterTopDownload = TopDownloadAdapter(this)
        binding.rcvTopDownload.adapter = adapterTopDownload
        binding.rcvTopDownload.layoutManager = GridLayoutManager(requireActivity(),2, RecyclerView.HORIZONTAL, false)

        homeViewModel.generalTopDownload.observe(viewLifecycleOwner){
            adapterTopDownload.mList = it.data as ArrayList<Music>
            adapterTopDownload.notifyDataSetChanged()
        }

        val adapterGenres = GenresAdapter(this)
        binding.rcvGenres.adapter = adapterGenres
        binding.rcvGenres.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)

        homeViewModel.generalGenres.observe(viewLifecycleOwner){
            adapterGenres.mList = it.data as ArrayList<Genres>
            adapterGenres.notifyDataSetChanged()
        }

        binding.lnSelectRegion.setOnClickListener {
            Toast.makeText(requireActivity(), "Comming soon!", Toast.LENGTH_SHORT).show()
        }

        homeViewModel.statusGeneraTrending.observe(viewLifecycleOwner){
            if (it){
                binding.progressBarHome.visibility = View.VISIBLE
                binding.progressBarHome.indeterminateDrawable = Wave()
            }else{
                binding.progressBarHome.visibility = View.GONE
            }
        }

        moreClickListened()
        return root
    }

    override fun clickPlayMusic(music: Music) {
        ManagerMusic.setCurrentMusic(music)
        ManagerMusicDownLoaded.currentMusicDownloaded = null
        sendActionToService(PlayMusicService.ACTION_START)
    }

    override fun itemGenresClick(genres: Genres) {
        val action = HomeFragmentDirections.actionHomeFragmentToMusicFromGenres(genres)
        requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
    }

    fun moreClickListened(){
        binding.tvMoreGenres.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToDetailGenresFragment()
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
        }

        binding.tvMoreTopRating.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailTopRatingFragment()
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
        }

        binding.tvMoreTopListened.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailTopLisFragment()
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
        }

        binding.tvMoreTopDownload.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailTopDownFragment()
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
        }

        binding.ivSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
        }
    }

    private fun playMusic(music: Music) {
        val intent = Intent(requireActivity(), PlayMusicService::class.java)
        val bundle = Bundle()
        bundle.putSerializable("song", music)
        intent.putExtras(bundle)
        requireActivity().startService(intent)
    }

    private fun sendActionToService(action: Int) {
        val intent = Intent(requireActivity(), PlayMusicService::class.java)
        intent.putExtra("action_music_service", action)
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}