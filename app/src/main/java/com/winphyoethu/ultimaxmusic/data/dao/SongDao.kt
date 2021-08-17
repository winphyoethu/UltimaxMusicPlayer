package com.winphyoethu.ultimaxmusic.data.dao

import androidx.room.*
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong
import com.winphyoethu.ultimaxmusic.data.entity.Song
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: Song): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongs(song: List<Song>): Completable

    @Delete
    fun deleteSong(song: Song): Completable

//    @Query("SELECT * FROM song WHERE playlist_id=:playlistId")
//    fun getSongsByPlaylistId(playlistId: Int) : Observable<List<Song>>

    @Query("SELECT * FROM song")
    fun getSongs(): Observable<MutableList<Song>>

    @Transaction
    @Query("SELECT * FROM playlist WHERE playlist_id = :playlistId ")
    fun getSongsByPlaylistId(playlistId: Int): Observable<PlaylistWithSong>

}