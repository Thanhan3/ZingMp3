package com.example.musicplayer.ui.discovery.foryou

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.repository.song.SongRepositoryImpl

class ForYouViewModel(
    private val songRepository: SongRepositoryImpl
) : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val top40Songs = songRepository.top40ForYouSongs.asLiveData()
    val songs: LiveData<List<Song>>
        get() = _songs

    fun setSongs(songs: List<Song>) {
        _songs.value = songs
    }

    class Factory(
        private val songRepository: SongRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ForYouViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ForYouViewModel(songRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}