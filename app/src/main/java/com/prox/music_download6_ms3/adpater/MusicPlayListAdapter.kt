package com.prox.music_download6_ms3.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.callback.ItemMusicPlayListClick
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.ItemTopListenedBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic

class MusicPlayListAdapter(private val callBack: ItemMusicPlayListClick) : RecyclerView.Adapter<MusicPlayListAdapter.MusicPlayListViewHolder>(){

    var mList = arrayListOf<Music>()

    class MusicPlayListViewHolder(binding: ItemTopListenedBinding): RecyclerView.ViewHolder(binding.root){
        val binding : ItemTopListenedBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPlayListViewHolder {
        val binding =
            ItemTopListenedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicPlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicPlayListViewHolder, position: Int) {
        val music = mList[position]

        holder.binding.tvNameSong.text = music.name
        holder.binding.tvNameSinger.text = music.artistName

        Glide.with(holder.binding.ivTopListened).load(music.image).into(holder.binding.ivTopListened)

        holder.binding.layoutItemTopLis.setOnClickListener {
            callBack.itemMusicPLClick(music)
            ManagerMusic.setListMusic(mList)
        }

        holder.binding.ivDot.setOnClickListener {
            callBack.dotClick(music)
        }
    }

    override fun getItemCount(): Int {
        if (mList != null){
            return mList.size
        }
        return 0
    }
}