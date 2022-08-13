package com.prox.music_download6_ms3.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prox.music_download6_ms3.callback.PlayListClick
import com.prox.music_download6_ms3.databinding.ItemPlayListBinding
import com.prox.music_download6_ms3.model.PlayList
import com.prox.music_download6_ms3.music.ManagerMusic

class PlayListAdapter(private val callBack: PlayListClick) : RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>(){

    var mList = arrayListOf<PlayList>()

    class PlayListViewHolder(binding: ItemPlayListBinding): RecyclerView.ViewHolder(binding.root){
        val binding: ItemPlayListBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val binding =
            ItemPlayListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val playList = mList[position]

        holder.binding.tvFavoriteSongs.text = playList.name

        holder.binding.layoutItemPlayList.setOnClickListener {
            callBack.itemPlaylistClick(playList)
            ManagerMusic.setListMusic(playList.listMusic)
        }
    }

    override fun getItemCount(): Int {
        if (mList != null){
            return mList.size
        }
        return 0
    }
}