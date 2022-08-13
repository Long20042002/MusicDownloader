package com.prox.music_download6_ms3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prox.music_download6_ms3.db.dao.MusicDao
import com.prox.music_download6_ms3.db.dao.PlayListDAO
import com.prox.music_download6_ms3.model.Music
import com.prox.music_download6_ms3.model.PlayList

@Database(
    entities = [PlayList::class, Music::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(ConverterListMusic::class)
abstract class MusicDatabase: RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "musicDB"
        private var instance: MusicDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MusicDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun PlayListDAO(): PlayListDAO
    abstract fun MusicDao(): MusicDao
}