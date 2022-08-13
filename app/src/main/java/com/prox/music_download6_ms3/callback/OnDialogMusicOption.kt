package com.prox.music_download6_ms3.callback

interface OnDialogMusicOption {
    fun onFavoriteClick(isFavorite: Boolean)
    fun onShareClick()
    fun onAddPlayListClick()
    fun onDownloadClick()
}