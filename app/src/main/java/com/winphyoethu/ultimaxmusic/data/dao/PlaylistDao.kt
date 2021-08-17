package com.winphyoethu.ultimaxmusic.data.dao

import androidx.room.*
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistSongCrossRef
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface PlaylistDao {

    @Insert
    fun createPlaylist(playlist: Playlist): Completable

    @Delete
    fun deletePlaylist(playlist: Playlist): Completable

    @Query("SELECT * FROM playlist")
    fun getPlaylist(): Observable<List<Playlist>>

    @Transaction
    @Query("SELECT * FROM playlist")
    fun getPlaylistWithSong(): Observable<List<PlaylistWithSong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistSong(playlistSongCrossRef: PlaylistSongCrossRef): Completable

    @Delete
    fun deleteSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef): Completable

}