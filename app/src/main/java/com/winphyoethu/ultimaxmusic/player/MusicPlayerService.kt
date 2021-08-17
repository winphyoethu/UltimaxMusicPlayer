package com.winphyoethu.ultimaxmusic.player

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.session.MediaSessionManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import com.winphyoethu.ultimaxmusic.R
import com.winphyoethu.ultimaxmusic.data.entity.Song
import com.winphyoethu.ultimaxmusic.domain.player.MusicNotification
import com.winphyoethu.ultimaxmusic.domain.player.MusicPlayer
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_PAUSE
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_PLAY
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_RESUME
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_SKIP_NEXT
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_SKIP_PREVIOUS
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_STOP
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_STOP_SERVICE
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_ACTION
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_ACTION_NAME
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_NEXT
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_PAUSE
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_PREVIOUS
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_RESUME
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_STOP_SERVICE
import com.winphyoethu.ultimaxmusic.util.Constants.SP_CURRENT_PLAYING
import com.winphyoethu.ultimaxmusic.util.Constants.SP_CURRENT_POSITION
import com.winphyoethu.ultimaxmusic.util.Constants.SP_CURRENT_SONG
import com.winphyoethu.ultimaxmusic.util.Constants.SP_CURRENT_SONG_ID
import com.winphyoethu.ultimaxmusic.util.Constants.SP_IS_PLAYLIST
import com.winphyoethu.ultimaxmusic.util.Constants.SP_LAST_POSITION
import com.winphyoethu.ultimaxmusic.util.Constants.SP_PLAYLIST_ID
import com.winphyoethu.ultimaxmusic.util.Constants.SP_PLAYLIST_NAME
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : Service() {

    private val musicBinder: MusicBinder by lazy {
        MusicBinder()
    }

    @Inject
    lateinit var musicPlayer: MusicPlayer

    @Inject
    lateinit var musicNotification: MusicNotification

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private val songList: MutableList<Song> = ArrayList()
    private var currentPosition: Int = -1

    private lateinit var mediaSessionManager: MediaSessionManager
    private lateinit var mediaSessionCompat: MediaSessionCompat
    private lateinit var transportControl: MediaControllerCompat.TransportControls

    private val activeSong: Song?
        get() = if (currentPosition < 0) {
            null
        } else {
            songList[currentPosition]
        }

    val publisher: PublishSubject<String> = PublishSubject.create()

    override fun onBind(intent: Intent?): IBinder {
        return musicBinder
    }

    inner class MusicBinder : Binder() {
        val service: MusicPlayerService
            get() = this@MusicPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("SERVICECREATE :: ", "service created")
        currentPosition = preferenceHelper.getInt(SP_CURRENT_POSITION, -1)
        musicPlayer.completeListener {
            skipNextSong()
        }

        musicNotification.createNotificationChannel()
        musicNotification.setMusicPlayerService(this)
        initMediaSession()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("SERVICEDESTROY :: ", "ONDESTROY")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIncomingActions(intent)
        return START_NOT_STICKY
    }

    private fun initMediaSession() {
        mediaSessionManager = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager

        mediaSessionCompat = MediaSessionCompat(this, "Ultimax Music Player")
        transportControl = mediaSessionCompat.controller.transportControls

        mediaSessionCompat.isActive = true
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        mediaSessionCompat.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                super.onPlay()

                musicPlayer.playSong()
            }

            override fun onPause() {
                super.onPause()

                musicPlayer.pauseSong()
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()

                skipPreviousSong()
                sendMusicActionBroadcast(BROADCAST_MUSIC_PREVIOUS)
                updateNotification()
            }

            override fun onSkipToNext() {
                super.onSkipToNext()

                skipNextSong()
                sendMusicActionBroadcast(BROADCAST_MUSIC_NEXT)
                updateNotification()
            }

            override fun onStop() {
                super.onStop()
            }

            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
//                buildNotification(Playing)
            }
        })
    }

    private fun updateMetadata() {
        val albumArt = BitmapFactory.decodeResource(resources, R.drawable.ic_song_placeholder)

        mediaSessionCompat.setMetadata(
            MediaMetadataCompat.Builder()
                .putBitmap(METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, getCurrentSong()?.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, getCurrentSong()?.album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, getCurrentSong()?.artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 10000)
                .build()
        )
    }

    private fun handleIncomingActions(playbackAction: Intent?) {
        if (playbackAction == null || playbackAction.action.isNullOrEmpty()) return
        when (playbackAction.action) {
            ACTION_PLAY -> {

            }
            ACTION_PAUSE -> {
                pauseSong()
                sendMusicActionBroadcast(BROADCAST_MUSIC_PAUSE)
            }
            ACTION_RESUME -> {
                resumeSong()
                sendMusicActionBroadcast(BROADCAST_MUSIC_RESUME)
            }
            ACTION_STOP -> {

            }
            ACTION_STOP_SERVICE -> {
                stopSelf()
                Log.i("ONSTOPSERVICE :: ", "stopself")
                sendMusicActionBroadcast(BROADCAST_MUSIC_STOP_SERVICE)
            }
            ACTION_SKIP_NEXT -> {
                skipNextSong()
            }
            ACTION_SKIP_PREVIOUS -> {
                skipPreviousSong()
            }
        }
    }

    fun addSongs(songList: List<Song>) {
        this.songList.clear()
        this.songList.addAll(songList)
    }

    fun playSong(position: Int) {
        musicPlayer.stopSong()
        currentPosition = position
        activeSong?.let {
            musicPlayer.setDataSource(it.data)
            preferenceHelper.putString(SP_CURRENT_SONG, it.title)
            preferenceHelper.putInt(SP_CURRENT_SONG_ID, it.id)
            preferenceHelper.putInt(SP_CURRENT_POSITION, currentPosition)
        }
        updateMetadata()

        musicPlayer.playSong()
        updateNotification()
    }

    fun resumePauseSong() {
        if (activeSong != null) {
            if (musicPlayer.isPlaying) {
                pauseSong()
            } else {
                resumeSong()
            }
        }
    }

    private fun resumeSong() {
        updateMetadata()

        if (musicPlayer.currentPosition < 0) {
            playSong(currentPosition)
        } else {
            try {
                if (musicPlayer.checkTrackInfo()) {
                    playSong(currentPosition)
                } else {
                    musicPlayer.resumeSong()
                }
            } catch (e: Exception) {
                Log.i("RESUMESONGERR :: ", e.toString())
            }
        }

        updateNotification()
    }

    private fun pauseSong() {
        musicPlayer.pauseSong()
        updateNotification()
    }

    fun skipNextSong() {
        if (activeSong != null) {
            if (currentPosition == songList.size - 1) {
                currentPosition = 0
            } else {
                currentPosition++
            }

            playSong(currentPosition)

            sendMusicActionBroadcast(BROADCAST_MUSIC_NEXT)
        }
    }

    fun skipPreviousSong() {
        if (activeSong != null) {
            if (currentPosition == 0) {
                currentPosition = songList.size - 1
            } else {
                currentPosition--
            }

            playSong(currentPosition)

            sendMusicActionBroadcast(BROADCAST_MUSIC_PREVIOUS)
        }
    }

    private fun updateNotification() {
        musicNotification.createNotification(
            musicPlayer.isPlaying, musicPlayer.currentPosition, mediaSessionCompat, getCurrentSong()
        )
    }

    private fun sendMusicActionBroadcast(action: String) {
        sendBroadcast(Intent(BROADCAST_MUSIC_ACTION).apply {
            putExtra(BROADCAST_MUSIC_ACTION_NAME, action)
        })
    }

    fun getCurrentSong(): Song? = activeSong

}