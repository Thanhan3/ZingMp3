package com.example.musicplayer.ui.library.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.song.Song

class RecentViewModel : ViewModel() {
    private val _recentSongs = MutableLiveData<List<Song>>()
    val recentSongs: LiveData<List<Song>> = _recentSongs

    fun setRecentSongs(songs: List<Song>) {
        _recentSongs.value = songs
    }
}