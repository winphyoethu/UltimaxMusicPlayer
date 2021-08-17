package com.winphyoethu.ultimaxmusic.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.winphyoethu.ultimaxmusic.R
import com.winphyoethu.ultimaxmusic.data.entity.Song
import com.winphyoethu.ultimaxmusic.databinding.ItemSongBinding

class SongAdapter(
    val songClickListener: (position: Int) -> Unit,
    val clickToAddPlaylistListener: (song: Song) -> Unit
) : ListAdapter<Song, SongAdapter.SongViewHolder>(object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.equal(newItem)
        }

        override fun getChangePayload(oldItem: Song, newItem: Song): Any? {
            return if (oldItem.title == newItem.title) {
                if (oldItem.isSelected == newItem.isSelected) {
                    if (oldItem.isPlaying == newItem.isPlaying) {
                        super.getChangePayload(oldItem, newItem)
                    } else {
                        newItem
                    }
                } else {
                    newItem
                }
            } else {
                super.getChangePayload(oldItem, newItem)
            }
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        super.onBindViewHolder(holder, position, emptyList())
    }

    override fun onBindViewHolder(
        holder: SongViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty() || payloads[0] !is Song) {
            val song = getItem(position)
            holder.bindSong(song)
        } else {
            val song = payloads[0] as Song
            holder.bindIsSelected(song)
        }
    }

    inner class SongViewHolder(val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                songClickListener(layoutPosition)
            }

            binding.root.setOnLongClickListener {
                clickToAddPlaylistListener(getItem(layoutPosition))
                true
            }
        }

        fun bindSong(song: Song) {
            binding.tvSongTitle.text = song.title
            binding.tvSongAlbum.text = song.album
            binding.tvSongNo.text = "${layoutPosition + 1}"

            if (song.isSelected) {
                binding.viewIsPlaying.setBackgroundResource(R.drawable.gradient_music_playing)
            } else {
                binding.viewIsPlaying.setBackgroundResource(0)
            }
        }

        fun bindIsSelected(song: Song) {
            if (song.isSelected) {
                binding.viewIsPlaying.setBackgroundResource(R.drawable.gradient_music_playing)
            } else {
                binding.viewIsPlaying.setBackgroundResource(0)
            }
        }

    }

}