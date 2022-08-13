package com.prox.music_download6_ms3.fragment

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.MainActivity
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.adpater.DownloadedAdapter
import com.prox.music_download6_ms3.adpater.NamePlayListAdapter
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.callback.MusicDownloadClick
import com.prox.music_download6_ms3.callback.NamePlayListClick
import com.prox.music_download6_ms3.databinding.FragmentDownloadedBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.model.MusicDownloaded
import com.prox.music_download6_ms3.model.PlayList
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.ManagerMusicDownLoading
import com.prox.music_download6_ms3.music.PlayMusicService
import com.prox.music_download6_ms3.ui.playlist.PlaylistViewModel
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception

class DownLoadedFragment : BaseFragment(), MusicDownloadClick, NamePlayListClick {

    private lateinit var binding: FragmentDownloadedBinding
    private lateinit var dialogAddMusicToPL: Dialog
    private lateinit var playlistViewModel: PlaylistViewModel

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    val adapter = DownloadedAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadedBinding.inflate(inflater, container, false)

        playlistViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[PlaylistViewModel::class.java]

        binding.rcvDownLoaded.adapter = adapter

        ManagerMusicDownLoaded.getMusicFromFile()
        adapter.mList = ManagerMusicDownLoaded.musicDowloadeds
        adapter.notifyDataSetChanged()

        return binding.root
    }

    override fun clickPlayMusic(musicDownloaded: MusicDownloaded) {
        ManagerMusicDownLoaded.currentMusicDownloaded = musicDownloaded
        ManagerMusic.setCurrentMusic(null)
        sendActionToService(PlayMusicService.ACTION_START)
    }

    override fun menuClick(musicDownloaded: MusicDownloaded) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_bottom_downloaded)

        val deleteMusic = dialog.findViewById<LinearLayout>(R.id.layout_remove_song_downloaded)

        val ivMusic = dialog.findViewById<ImageView>(R.id.ivMusicDownloaded)
        val tvNameSong = dialog.findViewById<TextView>(R.id.tvNameSongDownloaded)
        val tvNameSinger = dialog.findViewById<TextView>(R.id.tvNameSingerDownloaded)

        ivMusic.setImageBitmap(musicDownloaded.bitmap)
        tvNameSong.text = musicDownloaded.music
        tvNameSinger.text = musicDownloaded.artist

        deleteMusic.setOnClickListener {
            musicDownloaded.uri?.let { File(it).delete() }
            ManagerMusicDownLoaded.getMusicFromFile()
            adapter.mList = ManagerMusicDownLoaded.musicDowloadeds
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        val layout_add_playlist = dialog.findViewById<LinearLayout>(R.id.layout_add_play_list_dl)

        layout_add_playlist.setOnClickListener {
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

        val layout_set_ring_tone = dialog.findViewById<LinearLayout>(R.id.layout_set_ring_tone)

        layout_set_ring_tone.setOnClickListener {
            checkPermissionWrite(musicDownloaded)
        }

        val ivShare = dialog.findViewById<ImageView>(R.id.ivShareDownloaded)

        ivShare.setOnClickListener {
            shareMusic()
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

    private fun checkPermissionWrite(musicDownloaded: MusicDownloaded){
        val check: Boolean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            check = Settings.System.canWrite(context)
            if (check){
                setRingTone(musicDownloaded)
                val toast = Toast.makeText(context, "Set as ringtone successfully", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0 , 50)
                toast.show()
            }else{
                val toast = Toast.makeText(context, "Please allow Modify System Setting permission to change ringtone.",
                Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0 , 50)
                toast.show()
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + requireContext().packageName)
                startActivity(intent)
            }
        }
    }

    private fun setRingTone(musicDownloaded: MusicDownloaded): Boolean {
        val values = ContentValues()
        val file = File(musicDownloaded.uri!!)
        values.put(MediaStore.MediaColumns.TITLE, musicDownloaded.music)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val  newUri : Uri? = requireContext().contentResolver
                .insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
            try {
                requireContext().contentResolver.openOutputStream(newUri!!).use {
                        os -> val size = file.length().toInt()
                    val bytes = ByteArray(size)
                    try {
                        val buf = BufferedInputStream(FileInputStream(file))
                        buf.read(bytes, 0 , bytes.size)
                        buf.close()
                        os?.write(bytes)
                        os?.close()
                        os?.flush()
                    }catch (e: IOException){
                        return false
                    }
                }
            }catch (ignored: Exception){
                return false
            }
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri)
        }else{
            values.put(MediaStore.MediaColumns.DATA, file.absolutePath)
            val uri = MediaStore.Audio.Media.getContentUriForPath(file.absolutePath)
            requireActivity().contentResolver
                .delete(uri!!, MediaStore.MediaColumns.DATA + "=\"" + file.absolutePath + "\"", null)
            val newUri: Uri? = requireContext().contentResolver.insert(uri, values)
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri)
            MediaStore.Audio.Media.getContentUriForPath(file.absolutePath)?.let {
                requireContext().contentResolver
                    .insert(it, values)
            }
        }
        return true
    }

    override fun clickItemNamePL(playList: PlayList) {
        ManagerMusic.getCurrentMusic()?.let { playlistViewModel.updateMusicPL(playList, it) }
        dialogAddMusicToPL.dismiss()
    }

}