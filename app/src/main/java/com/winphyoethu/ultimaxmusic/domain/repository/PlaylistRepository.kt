package com.winphyoethu.ultimaxmusic.domain.repository

import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistSongCrossRef
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong
import io.reactivex.Completable
import io.reactivex.Observable

interface PlaylistRepository {

    fun createPlaylist(playlist: Playlist): Completable

    fun getPlaylists(): Observable<List<Playlist>>

    fun insertPlaylistSong(playlistSongCrossRef: PlaylistSongCrossRef): Completable

    fun deleteSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef): Completable

    fun getPlaylistWithSong(): Observable<List<PlaylistWithSong>>

}