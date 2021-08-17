package com.winphyoethu.ultimaxmusic.ui.playlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.data.entity.PlaylistWithSong
import com.winphyoethu.ultimaxmusic.databinding.ItemPlaylistBinding

class PlaylistAdapter(val playlistClickListener: (playlist: PlaylistWithSong) -> Unit) :
    ListAdapter<PlaylistWithSong, PlaylistAdapter.PlaylistViewHolder>(object :
        DiffUtil.ItemCallback<PlaylistWithSong>() {
        override fun areItemsTheSame(
            oldItem: PlaylistWithSong, newItem: PlaylistWithSong
        ): Boolean {
            return oldItem.playlist.id == newItem.playlist.id
        }

        override fun areContentsTheSame(
            oldItem: PlaylistWithSong, newItem: PlaylistWithSong
        ): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bindPlaylist(playlist)
    }

    inner class PlaylistViewHolder(val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                playlistClickListener(getItem(layoutPosition))
            }
        }

        fun bindPlaylist(playlist: PlaylistWithSong) {
            binding.tvPlaylistTitle.text = playlist.playlist.playlistTitle

            binding.tvPlaylistSongCount.text = if (playlist.totalDuration.isEmpty()) {
                playlist.songList.size.toString() + " Songs "
            } else {
                playlist.songList.size.toString() + " Songs . " + playlist.totalDuration + " Long"
            }

        }

    }

}