package com.winphyoethu.ultimaxmusic.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "playlist")
data class Playlist(
    @ColumnInfo(name = "playlist_id")
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "playlist_title") val playlistTitle: String,
    @ColumnInfo(name = "created_date") val createdDate: Long,
    @ColumnInfo(name = "modified_date") val modifiedDate: Long
) {

    fun equal(playlist: Playlist): Boolean {
        return this.id == playlist.id && this.playlistTitle == playlist.playlistTitle
    }

}