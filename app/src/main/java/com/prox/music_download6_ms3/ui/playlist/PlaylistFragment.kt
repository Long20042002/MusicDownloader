package com.prox.music_download6_ms3.ui.playlist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.adpater.PlayListAdapter
import com.prox.music_download6_ms3.callback.PlayListClick
import com.prox.music_download6_ms3.databinding.FragmentPlaylistBinding
import com.prox.music_download6_ms3.db.MusicDatabase
import com.prox.music_download6_ms3.fragment.PlayMusicFragment
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.model.PlayList
import com.prox.music_download6_ms3.ui.home.HomeFragmentDirections

class PlaylistFragment : Fragment(), PlayListClick {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    lateinit var playlistViewModel: PlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playlistViewModel = ViewModelProvider(requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[PlaylistViewModel::class.java]

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adpater = PlayListAdapter(this)
        binding.rcvPlayList.adapter = adpater
        binding.rcvPlayList.layoutManager = LinearLayoutManager(requireActivity())

        playlistViewModel.playLists.observe(viewLifecycleOwner){
            adpater.mList = it as ArrayList<PlayList>
            adpater.notifyDataSetChanged()
        }

        initEvent()

        return root
    }

    private fun initEvent(){
        binding.cvFavorite.setOnClickListener {
            val action = PlaylistFragmentDirections.actionPlaylistFragmentToMusicFavoriteFragment()
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
        }

        binding.ivAddPlayList.setOnClickListener {
            addPlayList()
        }
    }

    private fun addPlayList() {
        val dialog = Dialog(requireActivity(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.dialog_add_play_list)

        val tvCreate = dialog.findViewById<TextView>(R.id.tvCreate)
        val editNamePL = dialog.findViewById<EditText>(R.id.editNamePlayList)
        val tvCancel = dialog.findViewById<TextView>(R.id.tvCancelPL)

        tvCreate.setOnClickListener {
            val name : String = editNamePL.text.toString()

            if (name == ""){
                Toast.makeText(requireActivity(), "Hãy nhập tên playlist", Toast.LENGTH_SHORT).show()
            }else {
                val playList = PlayList(name, ArrayList())
                playlistViewModel.addPL(playList)
                dialog.dismiss()
            }
        }

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun itemPlaylistClick(playList: PlayList) {
        val action = PlaylistFragmentDirections.actionPlaylistFragmentToMusicPlayListFragment(playList)
        requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}