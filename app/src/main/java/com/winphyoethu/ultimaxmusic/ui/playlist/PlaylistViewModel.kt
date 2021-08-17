package com.winphyoethu.ultimaxmusic.ui.playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.domain.repository.PlaylistRepository
import com.winphyoethu.ultimaxmusic.util.PlaylistUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    val playlistRepository: PlaylistRepository, val playlistUtil: PlaylistUtil
) : ViewModel() {

    val playlistPublisher: PublishSubject<PlaylistState> = PublishSubject.create()

    private val compositeDisposable = CompositeDisposable()

    fun getPlaylist() {
        compositeDisposable.add(
            playlistRepository.getPlaylistWithSong()
                .map {
                    playlistUtil.playlistDurationMapper(it)
                    it
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.isEmpty()) {
                        playlistPublisher.onNext(EmptyPlaylist)
                    } else {
                        playlistPublisher.onNext(ShowPlaylist(it))
                    }
                }, {

                })
        )
    }

//    fun createPlaylist(playlistTitle: String) {
//        val playlist = Playlist(
//            playlistTitle = playlistTitle,
//            createdDate = calendar.timeInMillis, modifiedDate = calendar.timeInMillis
//        )
//        compositeDisposable.add(
//            playlistRepository.createPlaylist(playlist)
//                .subscribeOn(Schedulers.io())
//                .subscribe({
//                    playlistPublisher.onNext(PlaylistCreated)
//                }, {
//                    playlistPublisher.onNext(PlaylistError(it.message!!))
//                })
//        )
//    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}