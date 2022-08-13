package com.prox.music_download6_ms3.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.ItemTopListenedBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic

class TopListenedAdapter(private val callBack: MusicClick) : RecyclerView.Adapter<TopListenedAdapter.TopListenedViewHolder>(){

    var mList = arrayListOf<Music>()

    class TopListenedViewHolder(binding: ItemTopListenedBinding): RecyclerView.ViewHolder(binding.root){
        val binding : ItemTopListenedBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopListenedViewHolder {
        val binding =
            ItemTopListenedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopListenedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopListenedViewHolder, position: Int) {
        val music = mList[position]

        holder.binding.tvNameSong.text = music.name
        holder.binding.tvNameSinger.text = music.artistName

        Glide.with(holder.binding.ivTopListened).load(music.image).into(holder.binding.ivTopListened)

        holder.binding.layoutItemTopLis.setOnClickListener {
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