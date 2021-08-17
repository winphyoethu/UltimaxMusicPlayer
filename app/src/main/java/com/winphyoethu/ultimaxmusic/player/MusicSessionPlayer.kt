package com.winphyoethu.ultimaxmusic.player

import androidx.media.AudioAttributesCompat
import androidx.media2.common.MediaItem
import androidx.media2.common.MediaMetadata
import androidx.media2.common.SessionPlayer
import com.google.common.util.concurrent.ListenableFuture

class MusicSessionPlayer: SessionPlayer() {

    override fun play(): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun pause(): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun prepare(): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun seekTo(position: Long): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun setPlaybackSpeed(playbackSpeed: Float): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun setAudioAttributes(attributes: AudioAttributesCompat): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun getPlayerState(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getDuration(): Long {
        TODO("Not yet implemented")
    }

    override fun getBufferedPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getBufferingState(): Int {
        TODO("Not yet implemented")
    }

    override fun getPlaybackSpeed(): Float {
        TODO("Not yet implemented")
    }

    override fun setPlaylist(
        list: MutableList<MediaItem>,
        metadata: MediaMetadata?
    ): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun getAudioAttributes(): AudioAttributesCompat? {
        TODO("Not yet implemented")
    }

    override fun setMediaItem(item: MediaItem): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun addPlaylistItem(index: Int, item: MediaItem): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun removePlaylistItem(index: Int): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun replacePlaylistItem(index: Int, item: MediaItem): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun skipToPreviousPlaylistItem(): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun skipToNextPlaylistItem(): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun skipToPlaylistItem(index: Int): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun updatePlaylistMetadata(metadata: MediaMetadata?): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun setRepeatMode(repeatMode: Int): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun setShuffleMode(shuffleMode: Int): ListenableFuture<PlayerResult> {
        TODO("Not yet implemented")
    }

    override fun getPlaylist(): MutableList<MediaItem> {
        TODO("Not yet implemented")
    }

    override fun getPlaylistMetadata(): MediaMetadata? {
        TODO("Not yet implemented")
    }

    override fun getRepeatMode(): Int {
        TODO("Not yet implemented")
    }

    override fun getShuffleMode(): Int {
        TODO("Not yet implemented")
    }

    override fun getCurrentMediaItem(): MediaItem? {
        TODO("Not yet implemented")
    }

    override fun getCurrentMediaItemIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getPreviousMediaItemIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun getNextMediaItemIndex(): Int {
        TODO("Not yet implemented")
    }
}