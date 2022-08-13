package com.prox.music_download6_ms3.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.callback.MusicDownloadClick
import com.prox.music_download6_ms3.databinding.ItemDownloadedBinding
import com.prox.music_download6_ms3.model.MusicDownloaded
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded

class DownloadedAdapter(private val callBack : MusicDownloadClick): RecyclerView.Adapter<DownloadedAdapter.DownloadedViewHolder>() {

    var mList = arrayListOf<MusicDownloaded>()

    class DownloadedViewHolder(binding: ItemDownloadedBinding) : RecyclerView.ViewHolder(binding.root){
        val binding:ItemDownloadedBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadedViewHolder {
        val binding =
            ItemDownloadedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadedViewHolder, position: Int) {
        val msDownloaded = mList[position]

        holder.binding.tvNameSong.text = msDownloaded.music
        holder.binding.tvNameSinger.text = msDownloaded.artist
        holder.binding.ivDownloaded.setImageBitmap(msDownloaded.bitmap)

        holder.binding.layoutItemMusicDownload.setOnClickListener {
            callBack.clickPlayMusic(msDownloaded)
            ManagerMusicDownLoaded.musicDowloadeds = mList
        }

        holder.binding.ivMenuDownload.setOnClickListener {
            callBack.menuClick(msDownloaded)
        }
    }

    override fun getItemCount(): Int {
        if (mList != null){
            return mList.size
        }
        return 0
    }
}