package com.prox.music_download6_ms3.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prox.music_download6_ms3.model.PlayList

@Dao
interface PlayListDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPL(playList: PlayList)

    @Update
    fun updatePL(playList: PlayList)

    @Delete
    fun deletePL(playList: PlayList)

    @Query("SELECT * FROM table_playlist")
    fun getAllPlayList(): LiveData<List<PlayList>>

    @Query("SELECT * FROM table_playlist where id = :id")
    fun getPlaylistFromID(id: Int): PlayList
}