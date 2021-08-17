package com.winphyoethu.ultimaxmusic.domain.player

interface MusicPlayer {

    val isInitialized: Boolean

    val isPlaying: Boolean

    var currentPosition: Int

    var data: String

    fun setDataSource(data: String)

    fun playSong()

    fun pauseSong()

    fun stopSong()

    fun resumeSong()

    fun checkTrackInfo() : Boolean

    fun completeListener(completeListener: ()-> Unit)

}