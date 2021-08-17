package com.winphyoethu.ultimaxmusic.domain.player

import android.app.PendingIntent
import android.support.v4.media.session.MediaSessionCompat
import com.winphyoethu.ultimaxmusic.data.entity.Song
import com.winphyoethu.ultimaxmusic.player.MusicPlayerService

interface MusicNotification {

    fun setMusicPlayerService(service: MusicPlayerService)

    fun createNotificationChannel()

    fun createNotification(
        isPlaying: Boolean,
        position: Int,
        mediaSession: MediaSessionCompat,
        song: Song?
    )

    fun isNotificationCreated(): Boolean

    fun playbackAction(action: String): PendingIntent

}