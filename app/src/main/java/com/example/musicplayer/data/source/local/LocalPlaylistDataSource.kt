package com.example.musicplayer.data.source.local

import android.database.sqlite.SQLiteConstraintException
import com.example.musicplayer.data.model.playlist.Playlist
import com.example.musicplayer.data.model.playlist.PlaylistSongCrossRef
import com.example.musicplayer.data.model.playlist.PlaylistWithSongs
import com.example.musicplayer.data.source.PlaylistDataSource
import kotlinx.coroutines.flow.Flow

class LocalPlaylistDataSource(
    private val playlistDao: PlaylistDao
) : PlaylistDataSource.Local {
    override val playlists: Flow<List<Playlist>>
        get() = playlistDao.playlists

    override val allPlaylists: Flow<List<Playlist>>
        get() = playlistDao.allPlaylists

    override fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>> {
        return playlistDao.getAllPlaylistsWithSongs()
    }

    override fun getPlaylistsWithSongsByPlaylistId(playlistId: Int): Flow<PlaylistWithSongs> {
        return playlistDao.getPlaylistsWithSongsByPlaylistId(playlistId)
    }

    override suspend fun insertPlaylistSongCrossRef(obj: PlaylistSongCrossRef): Long {
        return try {
            playlistDao.insertPlaylistSongCrossRef(obj)
        } catch (e: SQLiteConstraintException) {
            -1L
        }
    }

    override suspend fun findPlaylistByName(name: String): Playlist? {
        return playlistDao.findPlaylistByName(name)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.insert(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.delete(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(playlist)
    }
}