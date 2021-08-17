package com.winphyoethu.ultimaxmusic.ui.playlistdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.winphyoethu.ultimaxmusic.data.entity.Playlist
import com.winphyoethu.ultimaxmusic.databinding.ItemPlaylistDialogBinding

class PlaylistDialogAdapter(val playlistClickListener: (position: Int) -> Unit) :
    ListAdapter<Playlist, PlaylistDialogAdapter.PlaylistDialogViewHolder>(object :
        DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.equal(newItem)
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistDialogViewHolder {
        return PlaylistDialogViewHolder(
            ItemPlaylistDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistDialogViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist)
    }

    inner class PlaylistDialogViewHolder(val binding: ItemPlaylistDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                playlistClickListener(layoutPosition)
            }
        }

        fun bind(playlist: Playlist) {
            binding.tvPlaylistTitle.text = playlist.playlistTitle
        }

    }

}