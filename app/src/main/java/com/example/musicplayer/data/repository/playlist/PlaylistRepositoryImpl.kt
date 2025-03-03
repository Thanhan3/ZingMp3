package com.example.musicplayer.data.repository.playlist

import com.example.musicplayer.data.model.playlist.Playlist
import com.example.musicplayer.data.model.playlist.PlaylistSongCrossRef
import com.example.musicplayer.data.model.playlist.PlaylistWithSongs
import com.example.musicplayer.data.source.PlaylistDataSource
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    private val localDataSource: PlaylistDataSource.Local
) : PlaylistRepository.Local, PlaylistRepository.Remote {
    override val playlists: Flow<List<Playlist>>
        get() = localDataSource.playlists

    override val allPlaylists: Flow<List<Playlist>>
        get() = localDataSource.allPlaylists


    override fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>> {
        return localDataSource.getAllPlaylistsWithSongs()
    }

    override fun getPlaylistsWithSongsByPlaylistId(playlistId: Int): Flow<PlaylistWithSongs> {
        return localDataSource.getPlaylistsWithSongsByPlaylistId(playlistId)
    }

    override suspend fun insertPlaylistSongCrossRef(obj: PlaylistSongCrossRef): Long {
        return localDataSource.insertPlaylistSongCrossRef(obj)
    }

    override suspend fun findPlaylistByName(name: String): Playlist? {
        return localDataSource.findPlaylistByName(name)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        localDataSource.createPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        localDataSource.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        localDataSource.updatePlaylist(playlist)
    }
}