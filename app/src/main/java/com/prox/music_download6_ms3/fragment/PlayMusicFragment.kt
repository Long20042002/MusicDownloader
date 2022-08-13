package com.prox.music_download6_ms3.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.adpater.NamePlayListAdapter
import com.prox.music_download6_ms3.callback.NamePlayListClick
import com.prox.music_download6_ms3.callback.OnDialogMusicOption
import com.prox.music_download6_ms3.databinding.FragmentMusicPlayBinding
import com.prox.music_download6_ms3.dialog.DialogFragmentMusicOption
import com.prox.music_download6_ms3.model.MesFromEventBus
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.model.PlayList
import com.prox.music_download6_ms3.music.*
import com.prox.music_download6_ms3.sharedPreferences.ManagerShared
import com.prox.music_download6_ms3.ui.playlist.PlaylistViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PlayMusicFragment : BaseFragment(), OnDialogMusicOption, NamePlayListClick {

    lateinit var binding: FragmentMusicPlayBinding
    private lateinit var mMusic: Music
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private lateinit var playlistViewModel: PlaylistViewModel
    private var isFavorite = false
    private lateinit var dialogAddMusicToPL: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        EventBus.getDefault().register(this)
        playlistViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[PlaylistViewModel::class.java]

        binding = FragmentMusicPlayBinding.inflate(inflater, container, false)

        val music: Music? = ManagerMusic.getCurrentMusic()
        if (music != null) {
            mMusic = music
        }

        binding.ivPlayMusic.setImageResource(R.drawable.ic_pause)
        statusPlayOrPause()
        initEvent()
        playMusic(music)
        initSeekBar()
        setStatusRepeatAndRamdom()

        return binding.root
    }


    private fun initEvent() {
        binding.ivRemoteMusic.setOnClickListener {
            pauseOrPlayMusic()
        }

        binding.ivPlayMusic.setOnClickListener {
            pauseOrPlayMusic()
        }

        binding.ivNext.setOnClickListener {
            sendActionToService(PlayMusicService.ACTION_NEXT)
        }

        binding.ivPrevious.setOnClickListener {
            sendActionToService(PlayMusicService.ACTION_PREVIOUS)
        }

        binding.ivRepeatMode.setOnClickListener {
            when (ManagerMusic.getRepeatStatus()) {
                RepeatMode.NoLoop -> {
                    binding.ivRepeatMode.setImageResource(R.drawable.ic_repeat)
                    ManagerMusic.setRepeatStatus(RepeatMode.LoopList)
                }
                RepeatMode.LoopList -> {
                    binding.ivRepeatMode.setImageResource(R.drawable.ic_repeatone)
                    ManagerMusic.setRepeatStatus(RepeatMode.LoopOne)
                }
                RepeatMode.LoopOne -> {
                    binding.ivRepeatMode.setImageResource(R.drawable.ic_norepeat)
                    ManagerMusic.setRepeatStatus(RepeatMode.NoLoop)
                }
            }
        }

        binding.ivRamdomMusic.setOnClickListener {
            if (ManagerMusic.isRandom) {
                ManagerMusic.isRandom = false
                binding.ivRamdomMusic.setImageResource(R.drawable.ic_ramdom)
            } else {
                ManagerMusic.isRandom = true
                binding.ivRamdomMusic.setImageResource(R.drawable.ic_ramdom_active)
            }
        }

        binding.ivSongRemote.setOnClickListener {
            showDialogBottomSheet()
        }

        if (ManagerMusic.getCurrentMusic()
                ?.let { playlistViewModel.checkMusicFavorite(it.id) } != null
        ) {
            binding.ivFavoriteMusic.setImageResource(R.drawable.ic_favorite_active)
        }

        binding.ivFavoriteMusic.setOnClickListener {
            var isFavorite = false
            if (ManagerMusic.getCurrentMusic()
                    ?.let { playlistViewModel.checkMusicFavorite(it.id) } != null
            ) {
                isFavorite = true
            }

            if (isFavorite) {
                ManagerMusic.getCurrentMusic()?.let { it1 -> playlistViewModel.deleteFR(it1) }
                Toast.makeText(requireActivity(), "Bài hát đã được xóa!", Toast.LENGTH_SHORT).show()
                binding.ivFavoriteMusic.setImageResource(R.drawable.ic_favorite)

            } else {
                ManagerMusic.getCurrentMusic()?.let { playlistViewModel.addFR(it) }
                Toast.makeText(requireActivity(), "Bài hát đã được thêm!", Toast.LENGTH_SHORT)
                    .show()
                binding.ivFavoriteMusic.setImageResource(R.drawable.ic_favorite_active)

            }
        }

        binding.ivAddToPlaylist.setOnClickListener {
            onAddPlayListClick()
        }

    }

    private fun showDialogBottomSheet() {
        val dialogFragmentMusicOption = ManagerMusic.getCurrentMusic()?.let {
            DialogFragmentMusicOption(this, it)
        }
        dialogFragmentMusicOption?.show(childFragmentManager, "ramdom")
    }

    private fun setStatusRepeatAndRamdom() {
        when (ManagerMusic.getRepeatStatus()) {
            RepeatMode.NoLoop -> {
                binding.ivRepeatMode.setImageResource(R.drawable.ic_norepeat)
            }
            RepeatMode.LoopList -> {
                binding.ivRepeatMode.setImageResource(R.drawable.ic_repeat)
            }
            RepeatMode.LoopOne -> {
                binding.ivRepeatMode.setImageResource(R.drawable.ic_repeatone)
            }
        }

        if (ManagerMusic.isRandom) {
            binding.ivRamdomMusic.setImageResource(R.drawable.ic_ramdom_active)
        } else {
            binding.ivRamdomMusic.setImageResource(R.drawable.ic_ramdom)
        }
    }

    private fun initSeekBar() {
        if (ManagerMusicDownLoaded.currentMusicDownloaded == null){
            binding.sbMusicPlay.max = ManagerMusic.getCurrentMusic()?.duration!!
        }
        else{
            binding.sbMusicPlay.max = ManagerMusicDownLoaded.currentMusicDownloaded?.duration!!.toInt()
        }

        handler.post(runnableForSeekBar)
        binding.sbMusicPlay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) ManagerMedia.setProgress(progress * 1000)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }


    private val runnableForSeekBar: Runnable by lazy {
        object : Runnable {
            override fun run() {
                val currentPosition: Int = ManagerMedia.getProgress() / 1000
                binding.sbMusicPlay.progress = currentPosition
                binding.tvTimePlayedMusic.text = formattedTime(currentPosition)
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun pauseOrPlayMusic() {
        if (ManagerMedia.isPlaying) {
            sendActionToService(PlayMusicService.ACTION_PAUSE)
        } else {
            sendActionToService(PlayMusicService.ACTION_RESUME)
        }
    }

    private fun formattedTime(currentPosition: Int): String {
        val minutes = currentPosition / 60
        val seconds = currentPosition % 60
        return if (seconds < 10) {
            "$minutes:0$seconds"
        } else {
            "$minutes:$seconds"
        }
    }

    private fun statusPlayOrPause() {
        if (ManagerMedia.isPlaying) {
            binding.ivRemoteMusic.setImageResource(R.drawable.pause_music)
            binding.ivPlayMusic.setImageResource(R.drawable.ic_pause)
        } else {
            binding.ivRemoteMusic.setImageResource(R.drawable.play_music)
            binding.ivPlayMusic.setImageResource(R.drawable.ic_play)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(mesFromEventBus: MesFromEventBus) {
        when (mesFromEventBus.ms) {
            PlayMusicService.ACTION_START -> {

            }
            PlayMusicService.ACTION_CLOSE -> {

            }
            PlayMusicService.ACTION_PAUSE -> {
                binding.ivRemoteMusic.setImageResource(R.drawable.play_music)
                binding.ivPlayMusic.setImageResource(R.drawable.ic_play)
            }
            PlayMusicService.ACTION_NEXT -> {
                sendActionToService(PlayMusicService.ACTION_START)
                setStatusRepeatAndRamdom()
                playMusic(ManagerMusic.getCurrentMusic())
            }
            PlayMusicService.ACTION_PREVIOUS -> {
                playMusic(ManagerMusic.getCurrentMusic())
            }
            PlayMusicService.ACTION_RESUME -> {
                binding.ivRemoteMusic.setImageResource(R.drawable.pause_music)
                binding.ivPlayMusic.setImageResource(R.drawable.ic_pause)
            }
        }
    }


    private fun playMusic(music: Music?) {
        binding.sbMusicPlay.progress = 0
        binding.tvTimePlayedMusic.text = formattedTime(0)
        if (music != null) {
            binding.tvNameSong.text = music.name
            binding.tvNameSinger.text = music.artistName
            binding.tvTotalTimePlay.text = formattedTime(music.duration)
            music.image.let {
                val imgUri = it.toUri().buildUpon().scheme("http").build()
                binding.ivMusicPlay.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                }
            }
        }
        else{
            val musicDownload = ManagerMusicDownLoaded.currentMusicDownloaded
            binding.tvNameSong.text = musicDownload?.music
            binding.tvNameSinger.text = musicDownload?.artist
            binding.tvTotalTimePlay.text = formattedTime(musicDownload?.duration!!.toInt())
            if (musicDownload.bitmap != null){
                binding.ivMusicPlay.setImageBitmap(musicDownload.bitmap)
            }else{
               binding.ivMusicPlay.setImageResource(R.drawable.ic_launcher_foreground)
            }

        }
        ManagerMedia.mediaPlayer?.setOnPreparedListener {
            ManagerMedia.mediaPlayer?.start()
        }
    }

    override fun onFavoriteClick(isFavorite: Boolean) {
        if (isFavorite){
            binding.ivFavoriteMusic.setImageResource(R.drawable.ic_favorite_active)
        }else{
            binding.ivFavoriteMusic.setImageResource(R.drawable.ic_favorite)
        }
    }

    override fun onShareClick() {

    }

    override fun onAddPlayListClick() {
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

    override fun onDownloadClick() {
        downloadMS(ManagerMusic.getCurrentMusic()!!)
        ManagerShared.setStatusDownloading(true)
    }

    override fun clickItemNamePL(playList: PlayList) {
        ManagerMusic.getCurrentMusic()?.let { playlistViewModel.updateMusicPL(playList, it) }
        dialogAddMusicToPL.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}