package com.winphyoethu.ultimaxmusic.util

import android.util.Log
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSongPosition
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelper
import java.sql.Time
import javax.inject.Inject

class PlaylistUtil @Inject constructor(val songUtil: SongUtil, val pHelper: PreferenceHelper) {

    fun changePlaylistSong(
        playlistWithSong: PlaylistWithSong
    ): PlaylistWithSongPosition {

        val isPlaylistPlaying = pHelper.getBoolean(Constants.SP_IS_PLAYLIST, false)
        val playlistId = pHelper.getInt(Constants.SP_PLAYLIST_ID, -1)
        val songId = pHelper.getInt(Constants.SP_CURRENT_SONG_ID, -1)
        val tempPSongPosition = if (isPlaylistPlaying) {
            pHelper.getInt(Constants.SP_LAST_POSITION, -1)
        } else {
            -1
        }

        var mySongPosition: Int = -1
        if (isPlaylistPlaying) {
            if (playlistId == playlistWithSong.playlist.id) {
                val songList = playlistWithSong.songList
                for (i in songList.indices) {
                    Log.i("FUCKSHIT :: ", songList.toString())

                    Log.i("FUCKSHIT :: ", songList[i].id.toString() + " :: " + songId)
                    if (songList[i].id == songId) {
                        if (tempPSongPosition >= 0) {
                            songList[tempPSongPosition] =
                                songUtil.changeBothStatusFalse(songList[tempPSongPosition])
                        }

                        songList[i] = songUtil.changeBothStatusTrue(songList[i])

                        mySongPosition = i
                        Log.i("FUCKSHIT :: ", tempPSongPosition.toString() + " :: ")
                        break
                    }
                }
            }
        }

        return PlaylistWithSongPosition(playlistWithSong, mySongPosition)
    }

    fun playlistDurationMapper(playlist: List<PlaylistWithSong>): List<PlaylistWithSong> {
        playlist.forEach {
            var timestampInMilli: Long = 0
            it.songList.forEach {
                timestampInMilli += it.duration
            }

            it.totalDuration = TimeUtil.timestampToDuration(timestampInMilli)
        }

        return playlist
    }

}