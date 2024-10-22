package com.example.musicplayer.ui.home

import android.service.carrier.CarrierMessagingService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.repository.album.AlbumRepositoryImpl
import com.example.musicplayer.data.repository.song.SongRepositoryImpl
import com.example.musicplayer.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val albumRepository = AlbumRepositoryImpl()
    private val songRepository = SongRepositoryImpl()

    private val _albums = MutableLiveData<List<Album>>()
    private val _songs = MutableLiveData<List<Song>>()


    val albums: LiveData<List<Album>>
        get() = _albums
    val songs: LiveData<List<Song>>
        get() = _songs

    init {
        loadSongs()
        loadAlbums()
    }

    private fun loadAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.loadAlbums(object : ResultCallBack<Result<List<Album>>> {
                override fun onResult(result: Result<List<Album>>) {
                    if (result is Result.Success) {
                        _albums.postValue(result.data)
                    } else {
                        _albums.postValue(emptyList())
                    }
                }
            })
        }
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