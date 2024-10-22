package com.example.musicplayer.ui.home.album.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.model.song.Song

class DetailAlbumViewModel :ViewModel() {
    private val _album = MutableLiveData<Album>()
    private val _songs = MutableLiveData<List<Song>>()
    val album: LiveData<Album>
        get() = _album
    val songs: LiveData<List<Song>>
        get() = _songs

    fun setAlbum(album: Album) {
        _album.value = album
    }

    fun extractSongs(album: Album, songs: List<Song>?) {
        songs?.let {
            val songsList = mutableListOf<Song>()
            for (songId in album.songs) {
                val songIndex = songs.indexOfFirst { it.id == songId }
                if (songIndex != -1) {
                    songsList.add(songs[songIndex])
                }
            }
            _songs.value = songsList
        }
    }
}