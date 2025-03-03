package com.example.musicplayer.ui.home.recommended

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.ItemSongBinding

class SongAdapter(
    private val onSongClickListener: OnSongClickListener,
    private val onSongOptionMenuClickListener: OnSongOptionMenuClickListener
) : Adapter<SongAdapter.ViewHolder>() {
    private val songs = mutableListOf<Song>()

    class ViewHolder(
        private val binding: ItemSongBinding,
        private val onSongClickListener: OnSongClickListener,
        private val onSongOptionMenuClickListener: OnSongOptionMenuClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song, position: Int) {
            binding.textItemSongTitle.text = song.title
            binding.textItemSongArtist.text = song.artist
            Glide.with(binding.root)
                .load(song.image)
                .error(R.drawable.ic_album)
                .into(binding.imageItemSongArtwork)
            binding.root.setOnClickListener {
                onSongClickListener.onSongClick(song, position)
            }
            binding.btnItemSongOption.setOnClickListener {
                onSongOptionMenuClickListener.onSongOptionMenuClick(position)
            }
        }

    }

    fun updateSongs(newSongs: List<Song>?) {
        newSongs?.let {
            val oldSize = songs.size
            songs.clear()
            songs.addAll(it)
            if (oldSize >= songs.size){
                notifyItemRangeRemoved(0, oldSize)
            }
            notifyItemRangeChanged(0 , songs.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var binding = ItemSongBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, onSongClickListener, onSongOptionMenuClickListener)
    }


    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position], position)
    }

    interface OnSongClickListener {
        fun onSongClick(song: Song, position: Int)
    }

    interface OnSongOptionMenuClickListener {
        fun onSongOptionMenuClick(position: Int)
    }
}