package com.winphyoethu.ultimaxmusic.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "playlist_song_ref", primaryKeys = ["playlist_id", "song_id"])
data class PlaylistSongCrossRef(
    @ColumnInfo(name = "playlist_id")
    val playlistId: Int,
    @ColumnInfo(name = "song_id")
    val songId: Int
)