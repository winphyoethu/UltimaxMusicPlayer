package com.winphyoethu.ultimaxmusic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.winphyoethu.ultimaxmusic.data.dao.PlaylistDao
import com.winphyoethu.ultimaxmusic.data.dao.SongDao
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistSongCrossRef
import com.winphyoethu.ultimaxmusic.data.entity.Song

@Database(entities = [Playlist::class, Song::class, PlaylistSongCrossRef::class], version = 1, exportSchema = true)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao

    abstract fun songDao(): SongDao

}