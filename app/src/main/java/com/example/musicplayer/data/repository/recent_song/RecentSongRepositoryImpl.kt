package com.example.musicplayer.data.repository.recent_song

import com.example.musicplayer.data.model.song.RecentSong
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.source.local.LocalRecentSongDataSource
import kotlinx.coroutines.flow.Flow

class RecentSongRepositoryImpl(
    private val localDataSource: LocalRecentSongDataSource
) : RecentSongRepository.Local, RecentSongRepository.Remote {
    override val recentSongs: Flow<List<Song>>
        get() = localDataSource.recentSongs

    override suspend fun insert(vararg recentSongs: RecentSong) {
        localDataSource.insert(*recentSongs)
    }
}