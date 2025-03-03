package com.example.musicplayer.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.musicplayer.data.model.playlist.Playlist
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.repository.playlist.PlaylistRepositoryImpl
import com.example.musicplayer.data.repository.recent_song.RecentSongRepositoryImpl
import com.example.musicplayer.data.repository.song.SongRepositoryImpl

class LibraryViewModel(
    private val recentSongRepository: RecentSongRepositoryImpl,
    private val songRepository: SongRepositoryImpl,
    private val playlistRepository: PlaylistRepositoryImpl
) : ViewModel() {
    val recentSongs: LiveData<List<Song>> = recentSongRepository.recentSongs.asLiveData()
    val favoriteSongs: LiveData<List<Song>> = songRepository.favoriteSongs.asLiveData()
    val playlists: LiveData<List<Playlist>> = playlistRepository.playlists.asLiveData()

    class Factory(
        private val recentSongRepository: RecentSongRepositoryImpl,
        private val songRepository: SongRepositoryImpl,
        private val playlistRepository: PlaylistRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LibraryViewModel(recentSongRepository, songRepository,playlistRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}