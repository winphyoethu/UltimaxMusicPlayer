package com.winphyoethu.ultimaxmusic.player

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.winphyoethu.ultimaxmusic.domain.player.MusicPlayer
import com.winphyoethu.ultimaxmusic.util.Constants.SP_CURRENT_PLAYING
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelper
import javax.inject.Inject

class MusicPlayerImpl @Inject constructor(val pHelper: PreferenceHelper) : MusicPlayer,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    override var isInitialized: Boolean = false
    override var isPlaying: Boolean = false
    override var currentPosition: Int = 0
    override var data: String = ""

    private lateinit var completeListener: () -> Unit
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }

    override fun setDataSource(data: String) {
        this.data = data
        mediaPlayer.reset()
        mediaPlayer.setDataSource(data)
        mediaPlayer.prepare()
    }

    //    @Synchronized
    override fun playSong() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            isPlaying = true
            pHelper.putBoolean(SP_CURRENT_PLAYING, true)
        }
    }

    //    @Synchronized
    override fun pauseSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            currentPosition = mediaPlayer.currentPosition
            isPlaying = false
            pHelper.putBoolean(SP_CURRENT_PLAYING, false)
        }
    }

    //    @Synchronized
    override fun stopSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            currentPosition = 0
            isPlaying = false
            pHelper.putBoolean(SP_CURRENT_PLAYING, false)
        }
    }

    //    @Synchronized
    override fun resumeSong() {
        if (!mediaPlayer.isPlaying) {
            Log.i("RESUMEPAUSESONG :: ", "FUCK")
            Log.i("flark :: ", "saphote")
            mediaPlayer.seekTo(currentPosition)
            mediaPlayer.start()
            isPlaying = true
            pHelper.putBoolean(SP_CURRENT_PLAYING, true)
        }
    }

    override fun checkTrackInfo(): Boolean {
        return data.isEmpty()
    }

    override fun completeListener(completeListener: () -> Unit) {
        this.completeListener = completeListener
    }

    override fun onCompletion(mp: MediaPlayer?) {
        stopSong()
        completeListener()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        when (what) {
            MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> Log.d(
                "MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK $extra"
            )
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.d(
                "MediaPlayer Error", "MEDIA ERROR SERVER DIED $extra"
            )
            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.d(
                "MediaPlayer Error", "MEDIA ERROR UNKNOWN $extra"
            )
        }
        return false
    }

    override fun onPrepared(mp: MediaPlayer?) {
        playSong()
    }

}