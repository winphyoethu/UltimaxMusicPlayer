package com.winphyoethu.ultimaxmusic.data.repositoryimpl

import com.winphyoethu.ultimaxmusic.data.dao.SongDao
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong
import com.winphyoethu.ultimaxmusic.data.entity.Song
import com.winphyoethu.ultimaxmusic.domain.repository.SongRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(val songDao: SongDao) : SongRepository {

    override fun saveSong(song: Song): Completable {
        return songDao.insertSong(song)
    }

    override fun saveSongs(songList: List<Song>): Completable {
        return songDao.insertSongs(songList)
    }

    override fun getSongs(): Observable<MutableList<Song>> {
        return songDao.getSongs()
    }

    override fun getSongsByPlaylistId(playlistId: Int): Observable<PlaylistWithSong> {
        return songDao.getSongsByPlaylistId(playlistId)
    }

}