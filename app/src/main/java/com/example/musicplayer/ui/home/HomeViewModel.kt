package com.example.musicplayer.ui.home

import android.service.carrier.CarrierMessagingService
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.repository.album.AlbumRepositoryImpl
import com.example.musicplayer.data.repository.recent_song.RecentSongRepositoryImpl
import com.example.musicplayer.data.repository.song.SongRepositoryImpl
import com.example.musicplayer.data.source.Result
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val songRepository: SongRepositoryImpl) : ViewModel() {
    private val albumRepository = AlbumRepositoryImpl()

    private val _albums = MutableLiveData<List<Album>>()
    private val _songs = MutableLiveData<List<Song>>()
    private val _localSongs = MutableLiveData<List<Song>>()


    val albums: LiveData<List<Album>>
        get() = _albums
    val songs: LiveData<List<Song>>
        get() = _songs
    val localSongs : LiveData<List<Song>>
        get() = _localSongs

    init {
        loadSongs()
        loadAlbums()
        loadLocalSongs()
    }

    private fun loadLocalSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val songs = songRepository.songs
            _localSongs.postValue(songs)
        }
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
                    } else {
                        _songs.postValue(emptyList())
                    }
                }
            })
        }
    }

    private fun extractSongs(): List<Song> {
        val results: MutableList<Song> = mutableListOf()
        val localSongs = localSongs.value
        if (localSongs == null) {
            _songs.value?.let { results.addAll(it) }
        } else if (_songs.value != null) {
            for (song in _songs.value!!) {
                if (!localSongs.contains(song)) {
                    results.add(song)
                }
            }
        }
        return results
    }

    fun saveSongsToDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val songToSave = extractSongs()
            if (songToSave.isNotEmpty()) {
                val songArray = songToSave.toTypedArray()
                songRepository.insert(*songArray)
            }
        }
    }

    class Factory(
        private val songRepository: SongRepositoryImpl
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(songRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}