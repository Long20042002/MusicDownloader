package com.prox.music_download6_ms3.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.callback.OnDialogMusicOption
import com.prox.music_download6_ms3.databinding.DialogBottomSheetBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic
import com.prox.music_download6_ms3.ui.playlist.PlaylistViewModel

class DialogFragmentMusicOption(
    private val callBack: OnDialogMusicOption,
    private val music: Music
) : BottomSheetDialogFragment() {

    private lateinit var playlistViewModel: PlaylistViewModel
    private var isFavorite = false


    private lateinit var binding: DialogBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Dialog_Bottom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        playlistViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[PlaylistViewModel::class.java]
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes!!.windowAnimations = R.style.dialog_animation
        binding = DialogBottomSheetBinding.inflate(layoutInflater, container, false)

        Glide.with(requireActivity()).load(music.image).into(binding.ivMusicOP)
        binding.tvNameSong.text = music.name
        binding.tvNameSinger.text = music.artistName

        if (ManagerMusic.getCurrentMusic()
                ?.let { playlistViewModel.checkMusicFavorite(it.id) } != null
        ) {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_active)
        }
        initEvent()

        return binding.root
    }

    private fun initEvent() {
        binding.ivFavorite.setOnClickListener {
            var isFavorite = false
            if (ManagerMusic.getCurrentMusic()
                    ?.let { playlistViewModel.checkMusicFavorite(it.id) } != null
            ) {
                isFavorite = true
            }

            if (isFavorite) {
                ManagerMusic.getCurrentMusic()?.let { it1 -> playlistViewModel.deleteFR(it1) }
                Toast.makeText(requireActivity(), "Bài hát đã được xóa!", Toast.LENGTH_SHORT).show()
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                isFavorite = false
            } else {
                ManagerMusic.getCurrentMusic()?.let { playlistViewModel.addFR(it) }
                Toast.makeText(requireActivity(), "Bài hát đã được thêm!", Toast.LENGTH_SHORT)
                    .show()
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_active)
                isFavorite = true
            }
            callBack.onFavoriteClick(isFavorite)
        }

        binding.ivShare.setOnClickListener {
            callBack.onShareClick()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            intent.putExtra(Intent.EXTRA_TEXT, ManagerMusic.getCurrentMusic()?.audio)
            startActivity(Intent.createChooser(intent, "Share link"))
        }

        binding.layoutAddPlayList.setOnClickListener {
            callBack.onAddPlayListClick()
        }

        binding.layoutDownloadSong.setOnClickListener {
            callBack.onDownloadClick()
        }
    }
}
//        val dialog = Dialog(requireActivity())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.dialog_bottom_sheet)
//
//        dialog.show()
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window?.attributes!!.windowAnimations = R.style.dialog_animation
//        dialog.window?.setGravity(Gravity.BOTTOM)