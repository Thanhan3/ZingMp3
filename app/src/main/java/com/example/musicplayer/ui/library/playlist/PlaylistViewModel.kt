package com.example.musicplayer.ui.library.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.playlist.Playlist
import com.example.musicplayer.data.model.playlist.PlaylistSongCrossRef
import com.example.musicplayer.data.model.playlist.PlaylistWithSongs
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.repository.playlist.PlaylistRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepositoryImpl
) : ViewModel() {
    private val _playlist = MutableLiveData<Playlist?>()
    private val _addResult = MutableLiveData<Boolean>()
    private val _playlistWithSongs = MutableLiveData<PlaylistWithSongs>()

    val playlists = playlistRepository.getAllPlaylistsWithSongs().asLiveData()

    val addResult: LiveData<Boolean>
        get() = _addResult

    val playlist: LiveData<Playlist?>
        get() = _playlist


    val playlistWithSongs: LiveData<PlaylistWithSongs>
        get() = _playlistWithSongs

    fun createNewPlaylist(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = Playlist(_id = 0, name = playlistName, createdAt = Date())
            playlistRepository.createPlaylist(playlist)
        }
    }

    fun findPlaylistByName(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = playlistRepository.findPlaylistByName(playlistName)
            _playlist.postValue(result)
        }
    }

    fun getPlaylistWithSongByPlaylistId(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = playlistRepository.getPlaylistsWithSongsByPlaylistId(playlistId)
            result.collectLatest {
                _playlistWithSongs.postValue(it)
            }
        }
    }

    fun createPlaylistSongCrossRef(playlist: Playlist, song: Song?) {
        if(song != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val playlistSongCrossRef = PlaylistSongCrossRef()
                playlistSongCrossRef.playlistId = playlist.id
                playlistSongCrossRef.songId = song.id
                val result = playlistRepository.insertPlaylistSongCrossRef(playlistSongCrossRef)
                _addResult.postValue(result != -1L)
            }
        } else {
            _addResult.postValue(false)
        }
    }

    class Factory(
        private val playlistRepository: PlaylistRepositoryImpl
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
                return PlaylistViewModel(playlistRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}