package com.winphyoethu.ultimaxmusic.ui.playlist

import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong

sealed class PlaylistState

data class ShowPlaylist(val playlist: List<PlaylistWithSong>) : PlaylistState()

object PlaylistDeleted : PlaylistState()

object EmptyPlaylist : PlaylistState()

data class PlaylistError(val message: String) : PlaylistState()
