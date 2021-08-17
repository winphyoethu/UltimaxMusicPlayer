package com.winphyoethu.ultimaxmusic.ui.playlistdialog

import com.winphyoethu.ultimaxmusic.data.entity.Playlist

sealed class PlaylistDialogState

data class ShowPlaylist(val playlist: List<Playlist>): PlaylistDialogState()

object ShowSongInserted: PlaylistDialogState()