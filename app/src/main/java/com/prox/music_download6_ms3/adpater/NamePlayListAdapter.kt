package com.prox.music_download6_ms3.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.callback.NamePlayListClick
import com.prox.music_download6_ms3.databinding.ItemNameLayoutBinding
import com.prox.music_download6_ms3.model.PlayList

class NamePlayListAdapter(private val callBack: NamePlayListClick): RecyclerView.Adapter<NamePlayListAdapter.NamePlayListViewHolder>() {

    var mList = arrayListOf<PlayList>()
    class NamePlayListViewHolder(binding: ItemNameLayoutBinding): RecyclerView.ViewHolder(binding.root){
        val binding: ItemNameLayoutBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamePlayListViewHolder {
        val binding =
            ItemNameLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NamePlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NamePlayListViewHolder, position: Int) {
        val playList = mList[position]

        holder.binding.tvNamePL.text = playList.name
        holder.binding.layoutItemPl.setOnClickListener {
            callBack.clickItemNamePL(playList)
        }

    }

    override fun getItemCount(): Int {
      if (mList != null){
          return mList.size
      }
        return 0
    }
}