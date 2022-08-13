package com.prox.music_download6_ms3.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.adpater.MusicPlayListAdapter
import com.prox.music_download6_ms3.adpater.TopListenedAdapter
import com.prox.music_download6_ms3.callback.ItemMusicPlayListClick
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.FragmentMusicFavoriteBinding
import com.prox.music_download6_ms3.databinding.FragmentMusicFromPlayListBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.model.PlayList
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.PlayMusicService
import com.prox.music_download6_ms3.ui.playlist.PlaylistViewModel

class MusicPlayListFragment: BaseFragment(), ItemMusicPlayListClick {
    private lateinit var playlistViewModel : PlaylistViewModel
    private val args: MusicPlayListFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playlistViewModel = ViewModelProvider(requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[PlaylistViewModel::class.java]

        val binding = FragmentMusicFromPlayListBinding.inflate(inflater, container , false)

        binding.tvMenuPlayList.setOnClickListener {
            dialogMenu()
        }

        binding.tvPlayAll.setOnClickListener {
            if (args.playlist.listMusic.size > 0){
                ManagerMusic.setCurrentMusic(args.playlist.listMusic[0])
                sendActionToService(PlayMusicService.ACTION_START)
            }
        }

        val adapter = MusicPlayListAdapter(this)
        binding.rcvMusicFromPL.adapter = adapter
        binding.rcvMusicFromPL.layoutManager = LinearLayoutManager(requireActivity())

        playlistViewModel.getPlayListByID(args.playlist.id)

        playlistViewModel.musicPL.observe(viewLifecycleOwner){
            adapter.mList = it.listMusic
            adapter.notifyDataSetChanged()
            binding.tvNamePlayList.text = it.name
        }

        binding.ivBackHome.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return binding.root
    }

    private fun dialogMenu() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_bottom_play_list)

        val editPL = dialog.findViewById<LinearLayout>(R.id.layout_edit_name_pl)
        val deletePL = dialog.findViewById<LinearLayout>(R.id.layout_delete_pl)

        editPL.setOnClickListener {
            dialogRename()
        }

        deletePL.setOnClickListener {
            val playList = args.playlist
            playlistViewModel.deletePL(playList)
            dialog.dismiss()
            (activity as MainActivity).onBackPressed()
        }



        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes!!.windowAnimations = R.style.dialog_animation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun dialogRename() {
        val dialog = Dialog(requireActivity(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.dialog_rename_play_list)

        val tvDone = dialog.findViewById<TextView>(R.id.tvDone)
        val editRename = dialog.findViewById<EditText>(R.id.editNamePlayListRename)
        val tvCancel = dialog.findViewById<TextView>(R.id.tvCancelED)

        tvDone.setOnClickListener {
            val nameED : String = editRename.text.toString()

            if (nameED == ""){
                Toast.makeText(requireActivity(), "Hãy nhập tên playlist", Toast.LENGTH_SHORT).show()
            }else {
                val playList = args.playlist
                playList.name = nameED
                playlistViewModel.updatePL(playList)
                playlistViewModel.getPlayListByID(args.playlist.id)
                dialog.dismiss()
            }
        }

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun dialogDeleteMusicPL(music: Music){
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_bottom_delete_music_pl)

        val deleteMSPL = dialog.findViewById<LinearLayout>(R.id.layout_delete_music_pl)


        deleteMSPL.setOnClickListener {
            val playList = args.playlist
            playList.listMusic.remove(music)
            playlistViewModel.updatePL(playList)
            playlistViewModel.getPlayListByID(args.playlist.id)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes!!.windowAnimations = R.style.dialog_animation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    override fun itemMusicPLClick(music: Music) {
        ManagerMusic.setCurrentMusic(music)
        ManagerMusicDownLoaded.currentMusicDownloaded = null
        sendActionToService(PlayMusicService.ACTION_START)
    }

    override fun dotClick(music: Music) {
        dialogDeleteMusicPL(music)
    }

}