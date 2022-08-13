package com.prox.music_download6_ms3.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.ItemTopTrendingBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic


class TrendingAdpater(private val callBack : MusicClick, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<TrendingAdpater.TrendingViewHolder>() {

    var mList = arrayListOf<Music>()

    class TrendingViewHolder(binding: ItemTopTrendingBinding): RecyclerView.ViewHolder(binding.root){
        val binding: ItemTopTrendingBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val binding =
            ItemTopTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val music = mList[position]

        holder.binding.tvNameSong.text = music.name
        holder.binding.tvNameSinger.text = music.artistName

        Glide.with(holder.binding.root.context).load(music.image).into(holder.binding.ivItemTopTrending)
        holder.binding.layoutItemTrending.setOnClickListener {
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

