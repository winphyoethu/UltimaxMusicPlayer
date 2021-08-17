package com.winphyoethu.ultimaxmusic.ui.playlistdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistSongCrossRef
import com.winphyoethu.ultimaxmusic.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class PlaylistDialogViewModel @Inject constructor(val playlistRepository: PlaylistRepository) :
    ViewModel() {

    val playlistPublisher: PublishSubject<PlaylistDialogState> = PublishSubject.create()
    private val playlist: MutableList<Playlist> = ArrayList()

    private val compositeDisposable = CompositeDisposable()

    fun getPlaylist() {
        compositeDisposable.add(
            playlistRepository.getPlaylists()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    playlist.addAll(it)
                    playlistPublisher.onNext(ShowPlaylist(it))
                }, {

                })
        )
    }

    fun addSongToPlaylist(songId: Int, position: Int) {
        val playlistSongCrossRef = PlaylistSongCrossRef(playlist[position].id, songId)
        compositeDisposable.add(
            playlistRepository.insertPlaylistSong(playlistSongCrossRef)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    playlistPublisher.onNext(ShowSongInserted)
                }, {
                    Log.i("PDIALOG :: ", it.toString())
                })
        )
    }

}