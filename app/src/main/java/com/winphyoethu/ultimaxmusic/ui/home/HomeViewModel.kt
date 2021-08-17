package com.winphyoethu.ultimaxmusic.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistSongCrossRef
import com.winphyoethu.ultimaxmusic.data.entity.Song
import com.winphyoethu.ultimaxmusic.domain.repository.PlaylistRepository
import com.winphyoethu.ultimaxmusic.domain.repository.SongRepository
import com.winphyoethu.ultimaxmusic.ui.miniplayer.*
import com.winphyoethu.ultimaxmusic.util.Constants.SP_CURRENT_SONG_ID
import com.winphyoethu.ultimaxmusic.util.Constants.SP_IS_PLAYLIST
import com.winphyoethu.ultimaxmusic.util.Constants.SP_LAST_POSITION
import com.winphyoethu.ultimaxmusic.util.Constants.SP_PLAYLIST_ID
import com.winphyoethu.ultimaxmusic.util.Constants.SP_PLAYLIST_NAME
import com.winphyoethu.ultimaxmusic.util.PlaylistUtil
import com.winphyoethu.ultimaxmusic.util.SongUtil
import com.winphyoethu.ultimaxmusic.util.contentresolver.ContentResolverUtil
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val songRepository: SongRepository, val playlistRepository: PlaylistRepository,
    val pHelper: PreferenceHelper, val contentResolverUtil: ContentResolverUtil,
    val songUtil: SongUtil, val playlistUtil: PlaylistUtil
) : ViewModel() {

    val songStatePublisher: PublishSubject<SongFragmentState> = PublishSubject.create()
    val miniPlayerStatePublisher: PublishSubject<MiniPlayerState> = PublishSubject.create()

    private val songList: MutableList<Song> = ArrayList()
    private val pSongList: MutableList<Song> = ArrayList()
    private lateinit var playlist: Playlist
    private var songPosition = -1
    private var pSongPosition = -1

    private val compositeDisposable = CompositeDisposable()
    private val playlistSongDisposable = CompositeDisposable()

    private val playlistId = pHelper.getInt(SP_PLAYLIST_ID, -1)
    private val songId = pHelper.getInt(SP_CURRENT_SONG_ID, -1)

    fun getLatestSongList() {
        if (songId >= 0) {
            if (playlistId >= 0) {
                compositeDisposable.add(
                    songRepository.getSongsByPlaylistId(playlistId)
                        .delay(300, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            miniPlayerStatePublisher.onNext(MiniPlayerAddSong(it.songList))
                        }, {

                        })
                )
            } else {
                compositeDisposable.add(
                    songRepository.getSongs()
                        .delay(300, TimeUnit.MILLISECONDS)
                        .subscribe({
                            miniPlayerStatePublisher.onNext(MiniPlayerAddSong(songList))
                        }, {
                            Log.i("FUCKDICKSSS :: ", it.toString())
                        })
                )
            }
        }
    }

    fun getSongs() {
        compositeDisposable.add(
            songRepository.getSongs()
                .map {
                    val songListWithPosition = songUtil.changeSong(it)
                    songPosition = songListWithPosition.position

                    songListWithPosition.songList
                }
                .subscribe({
                    if (it.isEmpty()) {
                        getSongsFromContentProvider()
                    } else {
                        songList.clear()
                        songList.addAll(it)
                        songStatePublisher.onNext(ShowSong(songList))
                    }
                }, {
                    Log.i("FUCKDICKSSS :: ", it.toString())
                })
        )
    }

    fun getPlaylistSong(playlistId: Int) {
        playlistSongDisposable.add(
            songRepository.getSongsByPlaylistId(playlistId)
                .map {
                    val playlistWithSongPosition = playlistUtil.changePlaylistSong(it)
                    pSongPosition = playlistWithSongPosition.position

                    playlistWithSongPosition.playlistWithSong
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    playlist = it.playlist
                    if (it.songList.isEmpty()) {
                        songStatePublisher.onNext(EmptySong)
                    } else {
                        pSongList.clear()
                        pSongList.addAll(it.songList)
                        songStatePublisher.onNext(ShowPlaylistSong(it.songList))
                    }
                }, {

                })
        )
    }

    fun disposePlaylistSong() {
        playlistSongDisposable.clear()
    }

    fun deleteSongFromPlaylist(songId: Int, playlistId: Int) {
        val playlistSongCrossRef = PlaylistSongCrossRef(playlistId, songId)
        compositeDisposable.add(
            playlistRepository.deleteSongFromPlaylist(playlistSongCrossRef)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    songStatePublisher.onNext(ShowSongDeleted)
                }, {

                })
        )
    }

    private fun getSongsFromContentProvider() {
        compositeDisposable.add(
            contentResolverUtil.getSongFromContentProvider()
                .subscribe({
                    saveSongToDatabase(it)
                    songList.addAll(it)
                    songStatePublisher.onNext(ShowSong(songList))
                }, {

                })
        )
    }

    private fun saveSongToDatabase(songList: List<Song>) {
        compositeDisposable.add(
            songRepository.saveSongs(songList)
                .subscribe({
                    Log.i("FUCKDIC :: ", "SAVED")
                }, {
                    Log.i("FUCKDIC :: ", it.toString())
                })
        )
    }

    fun playSelectedSong(position: Int) {
        pHelper.remove(SP_IS_PLAYLIST)
        pHelper.remove(SP_PLAYLIST_NAME)
        pHelper.remove(SP_PLAYLIST_ID)
        pSongPosition = -1

        miniPlayerStatePublisher.onNext(MiniPlayerAddSong(songList))

        if (songPosition == -1) {
            songList[position] = songUtil.changeBothStatusOfSong(songList[position])
            songPosition = position

            songStatePublisher.onNext(ShowSong(songList))

            miniPlayerStatePublisher.onNext(MiniPlayerPlaySong(position))
        } else {
            songList[songPosition] = songUtil.changeBothStatusOfSong(songList[songPosition])
            songList[position] = songUtil.changeBothStatusOfSong(songList[position])
            songPosition = position

            songStatePublisher.onNext(ShowSong(songList))

            miniPlayerStatePublisher.onNext(MiniPlayerPlaySong(position))
        }
        pHelper.putInt(SP_LAST_POSITION, songPosition)
    }

    fun updateSongPosition(isPrevious: Boolean) {
        val isPlaylistPlaying = pHelper.getBoolean(SP_IS_PLAYLIST, false)
        val position = if (isPrevious) {
            if (isPlaylistPlaying) {
                if (pSongPosition == 0) pSongList.size - 1 else pSongPosition - 1
            } else {
                if (songPosition == 0) songList.size - 1 else songPosition - 1
            }
        } else {
            if (isPlaylistPlaying) {
                if (pSongPosition == pSongList.size - 1) 0 else pSongPosition + 1
            } else {
                if (songPosition == songList.size - 1) 0 else songPosition + 1
            }
        }

        if (isPlaylistPlaying) {
            pSongList[pSongPosition] = songUtil.changeBothStatusOfSong(pSongList[pSongPosition])
            pSongList[position] = songUtil.changeBothStatusOfSong(pSongList[position])

            pSongPosition = position

            songStatePublisher.onNext(ShowPlaylistSong(pSongList))
        } else {
            songList[songPosition] = songUtil.changeBothStatusOfSong(songList[songPosition])
            songList[position] = songUtil.changeBothStatusOfSong(songList[position])

            songPosition = position

            songStatePublisher.onNext(ShowSong(songList))
        }
    }

    fun updatePlayPause() {
        val isPlaylistPlaying = pHelper.getBoolean(SP_IS_PLAYLIST, false)
        if (isPlaylistPlaying) {
            if (pSongPosition >= 0) {
                val playlistId = pHelper.getInt(SP_PLAYLIST_ID, -1)
                if (playlistId >= 0) {
                    if (playlistId == playlist.id) {
                        pSongList[pSongPosition] =
                            songUtil.changeIsPlayingOfSong(pSongList[pSongPosition])
                    }
                }
            }
        } else {
            if (songPosition >= 0) {
                songList[songPosition] = songUtil.changeIsPlayingOfSong(songList[songPosition])

                songStatePublisher.onNext(ShowSong(songList))
            }
        }
    }

    fun skipPreviousNext(isPrevious: Boolean) {
        val isPlaylistPlaying = pHelper.getBoolean(SP_IS_PLAYLIST, false)
        val position = if (isPrevious) {
            if (isPlaylistPlaying) {
                if (pSongPosition == 0) pSongList.size - 1 else pSongPosition - 1
            } else {
                if (songPosition == 0) songList.size - 1 else songPosition - 1
            }
        } else {
            if (isPlaylistPlaying) {
                if (pSongPosition == pSongList.size - 1) 0 else pSongPosition + 1
            } else {
                if (songPosition == songList.size - 1) 0 else songPosition + 1
            }
        }

        if (isPlaylistPlaying) {
            if (pSongList.isNotEmpty()) {
                pSongList[pSongPosition] = songUtil.changeBothStatusOfSong(pSongList[pSongPosition])
                pSongList[position] = songUtil.changeBothStatusOfSong(pSongList[position])

                pSongPosition = position

                songStatePublisher.onNext(ShowPlaylistSong(pSongList))
            } else {
                songStatePublisher.onNext(EmptySong)
            }
        } else {
            if (songList.isNotEmpty()) {
                if (songPosition >= 0) {
                    songList[songPosition] = songUtil.changeBothStatusOfSong(songList[songPosition])
                    songList[position] = songUtil.changeBothStatusOfSong(songList[position])

                    songPosition = position

                    songStatePublisher.onNext(ShowSong(songList))
                }
            } else {
                songStatePublisher.onNext(EmptySong)
            }
        }

        miniPlayerStatePublisher.onNext(MiniPlayerPlaySong(position))
    }

    fun playPauseSong() {
        val isPlaylistPlaying = pHelper.getBoolean(SP_IS_PLAYLIST, false)
        if (isPlaylistPlaying) {
            if (pSongPosition >= 0) {
                val playlistId = pHelper.getInt(SP_PLAYLIST_ID, -1)
                if (playlistId >= 0) {
                    if (playlistId == playlist.id) {
                        val isCurrentPlaying = pSongList[pSongPosition].isPlaying

                        pSongList[pSongPosition] =
                            songUtil.changeIsPlayingOfSong(pSongList[pSongPosition])

                        miniPlayerStatePublisher.onNext(MiniPlayerAddSong(pSongList))
                        if (isCurrentPlaying) {
                            miniPlayerStatePublisher.onNext(MiniPlayerPauseSong)
                        } else {
                            miniPlayerStatePublisher.onNext(MiniPlayerResumeSong)
                        }
                        songStatePublisher.onNext(ShowPlaylistSong(pSongList))
                    }
                }
            }
        } else {
            miniPlayerStatePublisher.onNext(MiniPlayerAddSong(songList))

            if (songPosition >= 0) {
                val isCurrentPlaying = songList[songPosition].isPlaying

                songList[songPosition] = songUtil.changeIsPlayingOfSong(songList[songPosition])

                if (isCurrentPlaying) {
                    miniPlayerStatePublisher.onNext(MiniPlayerPauseSong)
                } else {
                    miniPlayerStatePublisher.onNext(MiniPlayerResumeSong)
                }
                songStatePublisher.onNext(ShowSong(songList))
            } else {
                if (songList.size > 0) {
                    songPosition = 0

                    songList[songPosition] = songUtil.changeBothStatusOfSong(songList[songPosition])

                    songStatePublisher.onNext(ShowSong(songList))

                    miniPlayerStatePublisher.onNext(MiniPlayerPlaySong(songPosition))

                    pHelper.putInt(SP_LAST_POSITION, songPosition)
                } else {
                    songStatePublisher.onNext(EmptySong)
                }
            }
        }
    }

    fun playSelectedPlaylistSong(position: Int) {
        pHelper.putBoolean(SP_IS_PLAYLIST, true)

        miniPlayerStatePublisher.onNext(MiniPlayerAddSong(pSongList))
        if (pSongPosition == -1) {
            if (songPosition >= 0) {
                songList[songPosition] = songUtil.changeBothStatusOfSong(songList[songPosition])
                songPosition = -1
            }

            pHelper.putString(SP_PLAYLIST_NAME, playlist.playlistTitle)
            pHelper.putInt(SP_PLAYLIST_ID, playlist.id)

            pSongPosition = position

            pSongList[pSongPosition] = songUtil.changeBothStatusOfSong(pSongList[pSongPosition])

            songStatePublisher.onNext(ShowPlaylistSong(pSongList))

            miniPlayerStatePublisher.onNext(MiniPlayerPlaySong(position))
        } else {
            pSongList[pSongPosition] = songUtil.changeBothStatusOfSong(pSongList[pSongPosition])
            pSongList[position] = songUtil.changeBothStatusOfSong(pSongList[position])

            pSongPosition = position

            songStatePublisher.onNext(ShowPlaylistSong(pSongList))

            miniPlayerStatePublisher.onNext(MiniPlayerPlaySong(position))
        }
        pHelper.putInt(SP_LAST_POSITION, pSongPosition)
    }

}