package com.winphyoethu.ultimaxmusic.ui.miniplayer

import com.winphyoethu.ultimaxmusic.data.entity.Song

sealed class MiniPlayerState

data class MiniPlayerPlaySong(val position: Int): MiniPlayerState()

object MiniPlayerResumeSong: MiniPlayerState()

object MiniPlayerPauseSong: MiniPlayerState()

data class MiniPlayerAddSong(val songList: List<Song>): MiniPlayerState()