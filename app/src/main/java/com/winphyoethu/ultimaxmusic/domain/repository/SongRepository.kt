package com.winphyoethu.ultimaxmusic.domain.repository

import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong
import com.winphyoethu.ultimaxmusic.data.entity.Song
import io.reactivex.Completable
import io.reactivex.Observable

interface SongRepository {

    fun saveSong(song: Song): Completable

    fun saveSongs(songList: List<Song>): Completable

    fun getSongs(): Observable<MutableList<Song>>

    fun getSongsByPlaylistId(playlistId: Int): Observable<PlaylistWithSong>

}