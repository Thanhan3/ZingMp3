package com.example.musicplayer.data.source.local

import com.example.musicplayer.data.model.song.RecentSong
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.source.RecentSongDataSource
import kotlinx.coroutines.flow.Flow

class LocalRecentSongDataSource(
    private val recentSongDao: RecentSongDao
) : RecentSongDataSource.Local {
    override val recentSongs: Flow<List<Song>>
        get() = recentSongDao.recentSongs

    override suspend fun insert(vararg recentSongs: RecentSong) {
        recentSongDao.insert(*recentSongs)
    }
}