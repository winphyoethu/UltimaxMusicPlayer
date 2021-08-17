package com.winphyoethu.ultimaxmusic.data.entity

import androidx.room.*

@Entity(tableName = "song")
data class Song constructor(
    @ColumnInfo(name = "song_id")
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "album") val album: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "created_date") val createdDate: Long,
    @ColumnInfo(name = "modified_date") val modifiedDate: Long,
    @Ignore var isPlaying: Boolean = false,
    @Ignore var isSelected: Boolean = false
) {

    constructor(
        id: Int,
        title: String,
        data: String,
        album: String,
        artist: String,
        duration: Long,
        createdDate: Long,
        modifiedDate: Long
    ) : this(
        id, title, data, album, artist, duration, createdDate, modifiedDate, false, false
    )

    fun equal(song: Song): Boolean {

        return this.id == song.id && this.title == song.title && this.data == song.data && this.album == song.album && this.artist == song.artist && this.duration == song.duration && this.isPlaying == song.isPlaying && this.isSelected == song.isSelected

    }

}