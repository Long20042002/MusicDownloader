package com.prox.music_download6_ms3.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.adpater.DownloadingAdapter
import com.prox.music_download6_ms3.adpater.NamePlayListAdapter
import com.prox.music_download6_ms3.callback.MusicDownloadingClick
import com.prox.music_download6_ms3.callback.NamePlayListClick
import com.prox.music_download6_ms3.databinding.FragmentDownloadBinding
import com.prox.music_download6_ms3.databinding.FragmentDownloadingBinding
import com.prox.music_download6_ms3.model.MusicDownloading
import com.prox.music_download6_ms3.model.PlayList
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.ManagerMusicDownLoading
import com.prox.music_download6_ms3.sharedPreferences.ManagerShared
import com.prox.music_download6_ms3.ui.playlist.PlaylistViewModel
import java.io.File

class DownLoadingFragment : BaseFragment(), MusicDownloadingClick, NamePlayListClick {
    private lateinit var binding: FragmentDownloadingBinding
    private lateinit var playlistViewModel: PlaylistViewModel
    private val adapter = DownloadingAdapter(this)
    private lateinit var dialogAddMusicToPL: Dialog



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadingBinding.inflate(inflater, container, false)

        playlistViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[PlaylistViewModel::class.java]

        binding.rcvDownLoading.adapter = adapter

        adapter.mList = ManagerMusicDownLoading.listDownloading()
        adapter.notifyDataSetChanged()

        return binding.root
    }

    override fun pauseOrResumeClick(musicDownloading: MusicDownloading) {
        if (ManagerShared.getStatusDownloading() == true){
            ManagerMusicDownLoading.fetch!!.pause(musicDownloading.request.id)
            ManagerShared.setStatusDownloading(false)
        }else{
            ManagerMusicDownLoading.fetch!!.resume(musicDownloading.request.id)
            ManagerShared.setStatusDownloading(true)
        }
    }

    override fun menuMusicDownloadingClick(musicDownloading: MusicDownloading) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_bottom_downloading)

        val layout_favorite_downloading = dialog.findViewById<LinearLayout>(R.id.layout_favorite_downloading)
        val ivFavoriteDownloading = dialog.findViewById<ImageView>(R.id.ivFavoriteDownloading)
        val layout_remove_downloading = dialog.findViewById<LinearLayout>(R.id.layout_remove_song_downloading)

        val ivMusic = dialog.findViewById<ImageView>(R.id.ivMusicDownloading)
        val tvNameSong = dialog.findViewById<TextView>(R.id.tvNameSongDLing)
        val tvNameSinger = dialog.findViewById<TextView>(R.id.tvNameSingerDLing)

        Glide.with(dialog.context).load(musicDownloading.urlImage).into(ivMusic)
        tvNameSong.text = musicDownloading.music
        tvNameSinger.text = musicDownloading.artist

        if (ManagerMusic.getCurrentMusic()
                ?.let { playlistViewModel.checkMusicFavorite(it.id) } != null
        ) {
            ivFavoriteDownloading.setImageResource(R.drawable.ic_favorite_active)
        }
        layout_favorite_downloading.setOnClickListener {
            var isFavorite = false
            if (ManagerMusic.getCurrentMusic()
                    ?.let { playlistViewModel.checkMusicFavorite(it.id) } != null
            ) {
                isFavorite = true
            }

            if (isFavorite) {
                ManagerMusic.getCurrentMusic()?.let { it1 -> playlistViewModel.deleteFR(it1) }
                Toast.makeText(requireActivity(), "Bài hát đã được xóa!", Toast.LENGTH_SHORT).show()
                ivFavoriteDownloading.setImageResource(R.drawable.ic_favorite)
                isFavorite = false
            } else {
                ManagerMusic.getCurrentMusic()?.let { playlistViewModel.addFR(it) }
                Toast.makeText(requireActivity(), "Bài hát đã được thêm!", Toast.LENGTH_SHORT)
                    .show()
                ivFavoriteDownloading.setImageResource(R.drawable.ic_favorite_active)
                isFavorite = true
            }
        }

        layout_remove_downloading.setOnClickListener {
            ManagerMusicDownLoading.fetch!!.cancel(musicDownloading.request.id)
            ManagerMusicDownLoading.fetch!!.delete(musicDownloading.request.id)
            dialog.dismiss()
        }

        val layout_pause_downloading = dialog.findViewById<LinearLayout>(R.id.layout_pause_downloading)
        val ivPause = dialog.findViewById<ImageView>(R.id.ivPauseDownloading)
        val tvPauseDownloading = dialog.findViewById<TextView>(R.id.tvPauseDownloading)

        if (ManagerShared.getStatusDownloading() == true){
            ivPause.setImageResource(R.drawable.ic_pause_downloading)
            tvPauseDownloading.text = "Pause Downloading"
        }else{
            ivPause.setImageResource(R.drawable.ic_resume_downloading)
            tvPauseDownloading.text = "Resume Downloading"
        }

        layout_pause_downloading.setOnClickListener {
            if (ManagerShared.getStatusDownloading() == true){
                ivPause.setImageResource(R.drawable.ic_pause_downloading)
                tvPauseDownloading.text = "Pause Downloading"
                ManagerMusicDownLoading.fetch!!.pause(musicDownloading.request.id)
                ManagerShared.setStatusDownloading(false)
                adapter.notifyDataSetChanged()
                dialog.dismiss()

            }else{
                ivPause.setImageResource(R.drawable.ic_resume_downloading)
                tvPauseDownloading.text = "Resume Downloading"
                ManagerMusicDownLoading.fetch!!.resume(musicDownloading.request.id)
                ManagerShared.setStatusDownloading(true)
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        val ivShare = dialog.findViewById<ImageView>(R.id.ivShareDownloading)

        ivShare.setOnClickListener {
            shareMusic()
        }

        val layout_add_music_to_playlist = dialog.findViewById<LinearLayout>(R.id.layout_add_play_list_dling)

        layout_add_music_to_playlist.setOnClickListener {
            dialogAddMusicToPL = Dialog(requireActivity(), R.style.Theme_Dialog)
            dialogAddMusicToPL.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialogAddMusicToPL.setContentView(R.layout.dialog_add_music_to_pl)

            val rcvNamePL = dialogAddMusicToPL.findViewById<RecyclerView>(R.id.rcvPlayListName)
            val ivAddPL = dialogAddMusicToPL.findViewById<ImageView>(R.id.ivAddPL)
            val adpter = NamePlayListAdapter(this)
            rcvNamePL.adapter = adpter
            rcvNamePL.layoutManager = LinearLayoutManager(requireActivity())
            playlistViewModel.playLists.observe(viewLifecycleOwner) {
                adpter.mList = it as ArrayList<PlayList>
                adpter.notifyDataSetChanged()
            }

            ivAddPL.setOnClickListener {
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
            dialogAddMusicToPL.show()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes!!.windowAnimations = R.style.dialog_animation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    override fun clickItemNamePL(playList: PlayList) {
        ManagerMusic.getCurrentMusic()?.let { playlistViewModel.updateMusicPL(playList, it) }
        dialogAddMusicToPL.dismiss()
    }
}