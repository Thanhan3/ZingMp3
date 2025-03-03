package com.example.musicplayer.data.source

import com.example.musicplayer.data.model.song.RecentSong
import com.example.musicplayer.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface RecentSongDataSource {
    interface Local {
        val recentSongs: Flow<List<Song>>
        suspend fun insert(vararg recentSongs: RecentSong)
    }

    interface Remote {
        // TODO
    }
}