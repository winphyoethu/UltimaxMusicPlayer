package com.winphyoethu.ultimaxmusic.util

object Constants {

    const val SHARED_PREFERENCE = "ultimax_music"

    // PENDING INTENT ACTIONS RELATED CONSTANTS
    const val ACTION_PLAY = "com.winphyoethu.ultimaxmusic.ACTION_PLAY"
    const val ACTION_PAUSE = "com.winphyoethu.ultimaxmusic.ACTION_PAUSE"
    const val ACTION_RESUME = "com.winphyoethu.ultimaxmusic.ACTION_RESUME"
    const val ACTION_STOP = "com.winphyoethu.ultimaxmusic.ACTION_STOP"
    const val ACTION_STOP_SERVICE = "com.winphyoethu.ultimaxmusic.ACTION_STOP_SERVICE"
    const val ACTION_SKIP_NEXT = "com.winphyoethu.ultimaxmusic.ACTION_SKIP_NEXT"
    const val ACTION_SKIP_PREVIOUS = "com.winphyoethu.ultimaxmusic.ACTION_SKIP_PREVIOUS"

    // NOTIFICATION RELATED CONSTANTS
    const val NOTIFICATION_CHANNEL_ID = "ultimax_music_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Ultimax Music"
    const val NOTIFICATION_ID = 1001

    // SHARED PREFERENCES RELATED CONSTANTS
    const val SP_PLAYLIST_NAME = "sp_playlist_name"
    const val SP_PLAYLIST_ID = "sp_playlist_id"
    const val SP_CURRENT_SONG = "sp_current_song"
    const val SP_CURRENT_SONG_ID = "sp_current_song_id"
    const val SP_IS_PLAYLIST = "sp_is_playlist"
    const val SP_CURRENT_PLAYING = "sp_current_playing"
    const val SP_CURRENT_POSITION = "sp_current_position"
    const val SP_LAST_POSITION = "sp_last_position"

    // BROADCAST RELATED CONSTANTS
    const val BROADCAST_MUSIC_ACTION = "com.winphyoethu.ultimaxmusic.BROADCAST_MUSIC_ACTION"
    const val BROADCAST_MUSIC_ACTION_NAME =
        "com.winphyoethu.ultimaxmusic.BROADCAST_MUSIC_ACTION_NAME"
    const val BROADCAST_MUSIC_PAUSE = "com.winphyoethu.ultimaxmusic.PAUSE"
    const val BROADCAST_MUSIC_RESUME = "com.winphyoethu.ultimaxmusic.RESUME"
    const val BROADCAST_MUSIC_NEXT = "com.winphyoethu.ultimaxmusic.NEXT"
    const val BROADCAST_MUSIC_PREVIOUS = "com.winphyoethu.ultimaxmusic.PREVIOUS"
    const val BROADCAST_MUSIC_STOP_SERVICE = "com.winphyoethu.ultimaxmusic.STOP_SERVICE"

}