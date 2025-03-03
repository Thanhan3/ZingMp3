package com.example.musicplayer.ui.home.recommended

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.repository.song.SongRepositoryImpl
import kotlinx.coroutines.Dispatchers
import com.example.musicplayer.data.source.Result
import kotlinx.coroutines.launch

class RecommendedViewModel : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>>
        get() = _songs

    fun setSongs(songs: List<Song>) {
        _songs.postValue(songs)
    }
}