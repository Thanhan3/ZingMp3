package com.example.musicplayer.ui.home.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.databinding.ItemAlbumBinding
import com.example.musicplayer.ui.home.album.AlbumAdapter.*

class AlbumAdapter(private val listener: OnAlbumClickListener) : RecyclerView.Adapter<ViewHolder>() {
    private val albums = mutableListOf<Album>()
    class ViewHolder(
        private val binding: ItemAlbumBinding,
        private val listener: OnAlbumClickListener
        ) :RecyclerView.ViewHolder(binding.root){
            fun bind(album: Album){
                binding.textAlbumItem.text = album.name;
                Glide.with(binding.root)
                    .load(album.artwork)
                    .error(R.drawable.ic_album)
                    .into(binding.imgAlbumItem)
                binding.root.setOnClickListener{
                    listener.onAlbumClick(album)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.ViewHolder {
        val layoutInflater  = LayoutInflater.from(parent.context)
        val binding = ItemAlbumBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: AlbumAdapter.ViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }
    fun updateAlbums(newAlbums : List<Album>?){
        newAlbums?.let{
            val oldSize = albums.size
            albums.clear()
            albums.addAll(it)
//            if (oldSize>albums.size){
//                notifyItemRangeRemoved(0,oldSize)
//            }else{
//                notifyItemRangeInserted(0,albums.size)
//            }
            notifyItemRangeChanged(0 ,albums.size)
        }

    }
    interface OnAlbumClickListener{
        fun onAlbumClick(album : Album)
    }
}