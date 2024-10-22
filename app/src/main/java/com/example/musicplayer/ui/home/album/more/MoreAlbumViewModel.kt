package com.example.musicplayer.ui.home.album.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.album.Album

class MoreAlbumViewModel : ViewModel() {
    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    fun setAlbums(albums: List<Album>?) {
        albums?.let { albumList ->
            _albums.value = albumList.sortedBy { -it.size }
        }
    }
}