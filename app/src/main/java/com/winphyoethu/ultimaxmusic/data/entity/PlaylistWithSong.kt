package com.winphyoethu.ultimaxmusic.data.entity

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithSong(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlist_id", entity = Song::class, entityColumn = "song_id",
        associateBy = Junction(
            value = PlaylistSongCrossRef::class,
            parentColumn = "playlist_id",
            entityColumn = "song_id"
        )
    )
    val songList: MutableList<Song>,
    @Ignore var totalDuration: String
) {
    constructor(playlist: Playlist, songList: MutableList<Song>) : this(playlist, songList, "")
}

