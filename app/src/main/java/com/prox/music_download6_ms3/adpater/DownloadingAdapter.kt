package com.prox.music_download6_ms3.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prox.music_download6_ms3.R
import com.prox.music_download6_ms3.callback.MusicDownloadingClick
import com.prox.music_download6_ms3.databinding.ItemDownloadingBinding
import com.prox.music_download6_ms3.model.MusicDownloading
import com.prox.music_download6_ms3.music.ManagerMusicDownLoaded
import com.prox.music_download6_ms3.music.ManagerMusicDownLoading
import com.prox.music_download6_ms3.sharedPreferences.ManagerShared
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import java.util.*

class DownloadingAdapter(private val callBack: MusicDownloadingClick): RecyclerView.Adapter<DownloadingAdapter.DownloadingViewHolder>() {

    var mList = arrayListOf<MusicDownloading>()

    class DownloadingViewHolder(binding: ItemDownloadingBinding): RecyclerView.ViewHolder(binding.root){
        val binding: ItemDownloadingBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadingViewHolder {
        val binding =
            ItemDownloadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadingViewHolder, position: Int) {
        val msDownloading = mList[position]

        if (ManagerShared.getStatusDownloading() == true){
            holder.binding.ivPlayOrPauseDownload.setImageResource(R.drawable.ic_pause_douwnload)
        }else{
            holder.binding.ivPlayOrPauseDownload.setImageResource(R.drawable.ic_resume_download)
        }

        ManagerMusicDownLoading.fetch!!.addListener(object : FetchListener{
            override fun onAdded(download: Download) {
            }

            override fun onCancelled(download: Download) {
            }

            override fun onCompleted(download: Download) {
                if (msDownloading.request.id == download.id){
                    mList.remove(msDownloading)
                    notifyDataSetChanged()
                    ManagerMusicDownLoaded.getMusicFromFile()
                }
            }

            override fun onDeleted(download: Download) {
                if (msDownloading.request.id == download.id){
                    mList.remove(msDownloading)
                    notifyDataSetChanged()
                }
            }

            override fun onDownloadBlockUpdated(
                download: Download,
                downloadBlock: DownloadBlock,
                totalBlocks: Int
            ) {
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
                if (msDownloading.request.id == download.id){
                    ManagerMusicDownLoading.fetch!!.retry(msDownloading.request.id)
                }
            }

            override fun onPaused(download: Download) {
            }

            override fun onProgress(
                download: Download,
                etaInMilliSeconds: Long,
                downloadedBytesPerSecond: Long
            ) {
                if (msDownloading.request.id == download.id){
                    if (download.total <= 0){
                        ManagerMusicDownLoading.fetch!!.retry(msDownloading.request.id)
                    }

                    val progress = download.progress
                    val timeRemaining = download.etaInMilliSeconds
                    val minutes = ((timeRemaining/1000)%3600)/60
                    val second = ((timeRemaining/ 1000 ) % 60)
                    val timef = String.format(Locale.getDefault(), "%02d:%02d", minutes, second)
                    holder.binding.tvTimeRemaining.text = timef.toString().plus(" Sec Remaing")
                    holder.binding.tvStatusDownload.text = progress.toString().plus("%")
                    holder.binding.progressHorizontal.progress = progress
                }
            }

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
            }

            override fun onRemoved(download: Download) {
            }

            override fun onResumed(download: Download) {
            }

            override fun onStarted(
                download: Download,
                downloadBlocks: List<DownloadBlock>,
                totalBlocks: Int
            ) {

            }

            override fun onWaitingNetwork(download: Download) {
            }

        })

        holder.binding.tvNameSong.text = msDownloading.music
        holder.binding.tvNameSinger.text = msDownloading.artist
        Glide.with(holder.binding.ivDownloaded).load(msDownloading.urlImage).into(holder.binding.ivDownloaded)


        holder.binding.ivPlayOrPauseDownload.setOnClickListener {
            if (ManagerShared.getStatusDownloading() == true){
                holder.binding.ivPlayOrPauseDownload.setImageResource(R.drawable.ic_resume_download)
            }else{
                holder.binding.ivPlayOrPauseDownload.setImageResource(R.drawable.ic_pause_downloading)
            }
            callBack.pauseOrResumeClick(msDownloading)
        }

        holder.binding.ivDot.setOnClickListener {
            callBack.menuMusicDownloadingClick(msDownloading)
        }
    }

    override fun getItemCount(): Int {
        if (mList != null){
            return mList.size
        }
        return 0
    }
}