package com.prox.music_download6_ms3.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.ItemTopDownloadBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic

class TopDownloadAdapter( private val callBack: MusicClick)
    : RecyclerView.Adapter<TopDownloadAdapter.TopDownloadViewHolder>(){

    var mList = arrayListOf<Music>()

    class TopDownloadViewHolder(binding: ItemTopDownloadBinding): RecyclerView.ViewHolder(binding.root){
        val binding : ItemTopDownloadBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopDownloadViewHolder {
        val binding =
            ItemTopDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  TopDownloadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopDownloadViewHolder, position: Int) {
        val music = mList[position]

        holder.binding.tvNameSong.text = music.name
        holder.binding.tvNameSinger.text = music.artistName
        Glide.with(holder.binding.ivTopListened).load(music.image).into(holder.binding.ivTopListened)

        holder.binding.layoutTopDownload.setOnClickListener {
            callBack.clickPlayMusic(music)
            ManagerMusic.setListMusic(mList)
        }

    }

    override fun getItemCount(): Int {
        if (mList != null){
            return mList.size
        }
        return 0
    }
}