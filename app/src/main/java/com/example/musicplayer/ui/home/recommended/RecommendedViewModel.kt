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
    private val songRepository = SongRepositoryImpl()
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

    init {
        loadSongs()
    }

    private fun loadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.loadSongs(object : ResultCallBack<Result<List<Song>>> {
                override fun onResult(result: Result<List<Song>>) {
                    if (result is Result.Success) {
                        _songs.postValue(result.data)
                    }
                    else {
                        _songs.postValue(emptyList())
                    }
                }
            })
        }
    }

}