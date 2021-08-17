package com.winphyoethu.ultimaxmusic.data.entity

data class SongListWithPosition(
    val songList: MutableList<Song>,
    val position: Int
)