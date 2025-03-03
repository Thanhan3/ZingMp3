package com.example.musicplayer.ui.library.playlist.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.playlist.Playlist
import com.example.musicplayer.data.model.playlist.PlaylistWithSongs

class MorePlaylistViewModel : ViewModel() {
    private val _playlists = MutableLiveData<List<PlaylistWithSongs>>()

    val playlists: LiveData<List<PlaylistWithSongs>> = _playlists

    fun setPlaylists(playlists: List<PlaylistWithSongs>) {
        _playlists.value = playlists
    }
}