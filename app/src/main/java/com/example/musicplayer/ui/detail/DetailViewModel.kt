package com.example.musicplayer.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.song.Song

class DetailViewModel : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    private val _playlistName = MutableLiveData<String>()
    private val _screenName = MutableLiveData<String>()

    val songs: LiveData<List<Song>>
        get() = _songs

    val playlistName: LiveData<String>
        get() = _playlistName

    val screenName: LiveData<String>
        get() = _screenName

    fun setSongs(songs: List<Song>) {
        _songs.value = songs
    }

    fun setPlaylistName(name: String) {
        _playlistName.value = name
    }

    fun setScreenName(name: String) {
        _screenName.value = name
    }

}