package com.winphyoethu.ultimaxmusic.ui.home

import com.winphyoethu.ultimaxmusic.data.entity.Song

sealed class SongFragmentState

object EmptySong: SongFragmentState()

data class Error(val message: String) : SongFragmentState()

data class ShowSong(val songList: List<Song>) : SongFragmentState()

data class ShowPlaylistSong(val songList: List<Song>) : SongFragmentState()

object ShowSongDeleted : SongFragmentState()