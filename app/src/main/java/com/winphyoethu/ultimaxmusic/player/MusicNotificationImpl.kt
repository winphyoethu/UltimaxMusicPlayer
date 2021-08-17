package com.winphyoethu.ultimaxmusic.player

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.winphyoethu.ultimaxmusic.R
import com.winphyoethu.ultimaxmusic.data.entity.Song
import com.winphyoethu.ultimaxmusic.domain.player.MusicNotification
import com.winphyoethu.ultimaxmusic.util.Constants
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_PAUSE
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_PLAY
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_RESUME
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_SKIP_NEXT
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_SKIP_PREVIOUS
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_STOP
import com.winphyoethu.ultimaxmusic.util.Constants.ACTION_STOP_SERVICE
import com.winphyoethu.ultimaxmusic.util.Constants.NOTIFICATION_CHANNEL_ID
import com.winphyoethu.ultimaxmusic.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.winphyoethu.ultimaxmusic.util.Constants.NOTIFICATION_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MusicNotificationImpl @Inject constructor() : MusicNotification {

    private var isNotificationCreated: Boolean = false
    private lateinit var service: MusicPlayerService

    override fun createNotificationChannel() {
        val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager.IMPORTANCE_LOW
        } else {
            0
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannelCompat.Builder(NOTIFICATION_CHANNEL_ID, importance)
                    .setName(NOTIFICATION_CHANNEL_NAME)
                    .setDescription("This is Music")
                    .setShowBadge(true)
                    .build()

            val notificationManager = NotificationManagerCompat.from(service)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun createNotification(
        isPlaying: Boolean, position: Int, mediaSession: MediaSessionCompat, song: Song?
    ) {
        val notificationAction: Int
        val playPauseAction: PendingIntent
        if (isPlaying) {
            notificationAction = R.drawable.ic_pause
            playPauseAction = playbackAction(ACTION_PAUSE)
        } else {
            service.stopForeground(false)

            notificationAction = R.drawable.ic_play
            playPauseAction = if (position > 0) {
                playbackAction(ACTION_RESUME)
            } else {
                playbackAction(ACTION_PLAY)
            }
        }

        val notificationBuilder = NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setSmallIcon(R.drawable.ic_song_placeholder)
            .setContentTitle(song?.title)
            .setContentText(song?.artist)
            .setAutoCancel(false)
            .setDeleteIntent(playbackAction(ACTION_STOP_SERVICE))
            .addAction(
                R.drawable.ic_skip_previous, "PREVIOUS", playbackAction(ACTION_SKIP_PREVIOUS)
            )
            .addAction(notificationAction, "PLAY_PAUSE", playPauseAction)
            .addAction(R.drawable.ic_skip_next, "NEXT", playbackAction(ACTION_SKIP_NEXT))

        val notificationManager = NotificationManagerCompat.from(service)

        if (isNotificationCreated) {
            if (isPlaying) {
                service.startForeground(NOTIFICATION_ID, notificationBuilder.build())
            } else {
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        } else {
            service.startForeground(NOTIFICATION_ID, notificationBuilder.build())
        }

        if (!isNotificationCreated) {
            isNotificationCreated = true
        }
    }

    override fun setMusicPlayerService(service: MusicPlayerService) {
        this.service = service
    }

    override fun isNotificationCreated(): Boolean = isNotificationCreated

    override fun playbackAction(action: String): PendingIntent {
        val playbackAction = Intent(service, MusicPlayerService::class.java)
        playbackAction.action = action
        return PendingIntent.getService(service, 0, playbackAction, 0)
    }

}