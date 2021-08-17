package com.winphyoethu.ultimaxmusic.ui.createplaylist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.domain.repository.PlaylistRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(val playlistRepository: PlaylistRepository) :
    ViewModel() {

    private val calendar = Calendar.getInstance()

    private val compositeDisposable = CompositeDisposable()

    fun createPlaylist(playlistTitle: String) {

        val playlist = Playlist(
            playlistTitle = playlistTitle,
            createdDate = calendar.timeInMillis,
            modifiedDate = calendar.timeInMillis
        )

        compositeDisposable.add(
            playlistRepository.createPlaylist(playlist)
                .subscribeOn(Schedulers.io())
                .subscribe({

                }, {

                })
        )

    }

}