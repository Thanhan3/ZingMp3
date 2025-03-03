package com.example.musicplayer.ui.searching

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.search.HistorySearchedKey
import com.example.musicplayer.data.model.search.HistorySearchedSong
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.repository.searching.SearchingRepository
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import com.example.musicplayer.utils.MusicAppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

class SearchingViewModel(
    private val repository: SearchingRepository
) : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    private val _searchedKey = MutableLiveData<String>()

    val historySearchedSongs: LiveData<List<HistorySearchedSong>>
        get() = repository.allSongs.asLiveData()
    val searchedKey: LiveData<String>
        get() = _searchedKey
    val songs: MutableLiveData<List<Song>>
        get() = _songs
    val keys: LiveData<List<HistorySearchedKey>>
        get() = repository.allKeys.asLiveData()


    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.search(query)
            result.collectLatest { songList ->
                _songs.postValue(songList)
            }
        }
    }

    fun insertSearchedKey(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val keyObj = HistorySearchedKey(0, key.trim(), Date())
            repository.insertKey(listOf(keyObj))
        }
    }

    fun insertSelectedSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            val historySearchedSong = HistorySearchedSong
                .Builder(song)
                .build()
            repository.insertSong(listOf(historySearchedSong))
        }
    }

    fun setSelectedKey(key: String) {
        _searchedKey.value = key
    }

    fun clearHistoryKeys() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllKeys()
        }
    }

    fun clearSearchedSongsHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllSongs()
        }
    }

    fun updatePlaylist(song: Song) {
        val playlistName = MusicAppUtils.DefaultPlaylistName.SEARCH.value

        val playlist = SharedViewModel.instance!!.getPlaylist(playlistName)
        val songs = mutableListOf<Song>()
        playlist?.songs?.let {
            songs.addAll(it)
        }
        if (songs.isEmpty()) {
            songs.add(song)
        } else {
            val songIndex = songs.indexOf(song)
            if (songIndex > 0) {
                songs.removeAt(songIndex)
            } else if (songIndex == -1) {
                songs.add(0, song)
            }
        }
        playlist?.updateSongList(songs)
        playlist?.let {
            SharedViewModel.instance!!.addPlaylist(it.name)
            SharedViewModel.instance!!.setupPlaylist(it.songs, it.name)
        }
    }

    class Factory(
        private val repository: SearchingRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchingViewModel::class.java)) {
                return SearchingViewModel(repository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}