package com.prox.music_download6_ms3.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.callback.MusicClick
import com.prox.music_download6_ms3.databinding.ItemTopRatingBinding
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.music.ManagerMusic

class TopRatingAdapter(private val callBack: MusicClick) :
    RecyclerView.Adapter<TopRatingAdapter.TopRatingViewHolder>() {

    var mList = arrayListOf<Music>()
    private val limit : Int = 5

    class TopRatingViewHolder(binding: ItemTopRatingBinding): RecyclerView.ViewHolder(binding.root) {
        val binding: ItemTopRatingBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatingViewHolder {
        val binding =
            ItemTopRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopRatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopRatingViewHolder, position: Int) {
        val music = mList[position]

        holder.binding.tvNameSongTopRating.text = music.name
        holder.binding.tvNameSingerTopRating.text = music.artistName

        Glide.with(holder.binding.root.context).load(music.image).into(holder.binding.ivTopRating)

        holder.binding.cstrTopRating.setOnClickListener {
            callBack.clickPlayMusic(music)
            ManagerMusic.setListMusic(mList)
        }

        when(position){
            0 -> {holder.binding.lnTopRating.background = ContextCompat.getDrawable(holder.binding.root.context,R.drawable.custom_shape_tl_of_top_1_rating)
                    holder.binding.tvTopRating.text = "1"}
            1 -> {holder.binding.lnTopRating.background = ContextCompat.getDrawable(holder.binding.root.context,R.drawable.custom_shape_tl_of_top_2_rating)
                holder.binding.tvTopRating.text = "2"}
            2 -> {holder.binding.lnTopRating.background = ContextCompat.getDrawable(holder.binding.root.context,R.drawable.custom_shape_tl_of_top_3_rating)
                holder.binding.tvTopRating.text = "3"}
            3 -> {holder.binding.lnTopRating.background = ContextCompat.getDrawable(holder.binding.root.context,R.drawable.custom_shape_tl_of_top_4_5_rating)
                holder.binding.tvTopRating.text = "4"}
            4 -> {holder.binding.lnTopRating.background = ContextCompat.getDrawable(holder.binding.root.context,R.drawable.custom_shape_tl_of_top_4_5_rating)
                holder.binding.tvTopRating.text = "5"}
        }
    }

    override fun getItemCount(): Int {
        if (mList != null){
            return if (mList.size > 5) 5 else mList.size
        }
        return 0;
    }
}