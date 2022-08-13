package com.prox.music_download6_ms3.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prox.music_download6_ms3.model.Music

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMS(music: Music)

    @Update
    fun updateMS(music: Music)

    @Delete
    fun deleteMS(music: Music)

    @Query("SELECT * FROM table_music_favorite")
    fun getAllMusicFavorite(): LiveData<List<Music>>

    @Query("SELECT * FROM table_music_favorite where id = :id")
    fun getMusic(id: String): Music?
}