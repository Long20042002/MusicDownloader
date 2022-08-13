package com.prox.music_download6_ms3.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.callback.GenresClick
import com.prox.music_download6_ms3.databinding.ItemDetailGenresBinding
import com.prox.music_download6_ms3.databinding.ItemGenresBinding
import com.prox.music_download6_ms3.model.Genres

class DetailGenresAdatper(private val callBack : GenresClick): RecyclerView.Adapter<DetailGenresAdatper.GenresViewHolder>() {

    var mList = arrayListOf<Genres>()

    class GenresViewHolder(binding: ItemDetailGenresBinding): RecyclerView.ViewHolder(binding.root){
        val binding: ItemDetailGenresBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val binding =
            ItemDetailGenresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val genres = mList[position]

        holder.binding.tvNameGenres.text = genres.name
        Glide.with(holder.binding.root.context).load("http://morpheustation.com/img-musicdownloader/${genres.image}").into(holder.binding.ivGenres)

        holder.binding.layoutItemGenres.setOnClickListener {
            callBack.itemGenresClick(genres)
        }
    }

    override fun getItemCount(): Int {
        if (mList != null){
            return mList.size
        }
        return 0
    }
}