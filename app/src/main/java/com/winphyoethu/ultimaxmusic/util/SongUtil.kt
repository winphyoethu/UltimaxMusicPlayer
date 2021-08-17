package com.winphyoethu.ultimaxmusic.util

import android.util.Log
import com.winphyoethu.ultimaxmusic.data.entity.Song
import com.winphyoethu.ultimaxmusic.data.entity.SongListWithPosition
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelper
import javax.inject.Inject

class SongUtil @Inject constructor(val pHelper: PreferenceHelper) {

    fun changeBothStatusOfSong(song: Song): Song {
        val isCurrentSelected = song.isSelected
        val isCurrentPlaying = song.isPlaying

        return song.copy(isSelected = !isCurrentSelected, isPlaying = !isCurrentPlaying)
    }

    fun changeBothStatusFalse(song: Song): Song {
        return song.copy(isSelected = false, isPlaying = false)
    }

    fun changeBothStatusTrue(song: Song): Song {
        return song.copy(isSelected = true, isPlaying = true)
    }

    fun changeIsPlayingOfSong(song: Song): Song {
        val isCurrentPlaying = song.isPlaying
        return song.copy(isPlaying = !isCurrentPlaying)
    }

    private fun changeSelectedAndPlay(song: Song): Song {
        return song.copy(isSelected = true, isPlaying = false)
    }

    fun changeSong(songList: MutableList<Song>): SongListWithPosition {
        val isPlaylistPlaying = pHelper.getBoolean(Constants.SP_IS_PLAYLIST, false)
        val isCurrentPlaying = pHelper.getBoolean(Constants.SP_CURRENT_PLAYING, false)
        val songId = pHelper.getInt(Constants.SP_CURRENT_SONG_ID, -1)
        val lastSongPosition = if (!isPlaylistPlaying) {
            pHelper.getInt(Constants.SP_LAST_POSITION, -1)
        } else {
            -1
        }
        var mySongPosition = -1

        if (!isPlaylistPlaying) {
            if (songId >= 0) {
                for (i in songList.indices) {
                    Log.i("FUCKSHIT :: ", songList.toString())

                    Log.i("FUCKSHIT :: ", songList[i].id.toString() + " :: " + songId)
                    if (songList[i].id == songId) {
                        if (lastSongPosition >= 0) {
                            if (lastSongPosition != i) {
                                songList[lastSongPosition] =
                                    changeBothStatusFalse(songList[lastSongPosition])
                            }
                        }

                        if (isCurrentPlaying) {
                            songList[i] = changeBothStatusTrue(songList[i])
                        } else {
                            songList[i] = changeSelectedAndPlay(songList[i])
                        }

                        mySongPosition = i
                        Log.i("FUCKSHIT :: ", songList[i].toString() + " :: ")
                        Log.i("FUCKSHIT :: ", songList[lastSongPosition].toString() + " :: ")
                        break
                    }
                }
            }
        }

        return SongListWithPosition(songList, mySongPosition)
    }

}