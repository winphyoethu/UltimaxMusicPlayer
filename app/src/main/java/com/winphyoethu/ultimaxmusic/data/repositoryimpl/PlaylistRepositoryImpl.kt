package com.winphyoethu.ultimaxmusic.data.repositoryimpl

import com.winphyoethu.ultimaxmusic.data.dao.PlaylistDao
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistSongCrossRef
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong
import com.winphyoethu.ultimaxmusic.domain.repository.PlaylistRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(val playlistDao: PlaylistDao): PlaylistRepository {

    override fun createPlaylist(playlist: Playlist): Completable {
        return playlistDao.createPlaylist(playlist)
    }

    override fun getPlaylists(): Observable<List<Playlist>> {
        return playlistDao.getPlaylist()
    }

    override fun insertPlaylistSong(playlistSongCrossRef: PlaylistSongCrossRef): Completable {
        return playlistDao.insertPlaylistSong(playlistSongCrossRef)
    }

    override fun deleteSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef): Completable {
        return playlistDao.deleteSongFromPlaylist(playlistSongCrossRef)
    }

    override fun getPlaylistWithSong(): Observable<List<PlaylistWithSong>> {
        return playlistDao.getPlaylistWithSong()
    }

}