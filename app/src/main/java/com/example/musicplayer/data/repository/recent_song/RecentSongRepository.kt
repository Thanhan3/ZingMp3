package com.example.musicplayer.data.repository.recent_song

import com.example.musicplayer.data.model.song.RecentSong
import com.example.musicplayer.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface RecentSongRepository {
    interface Local {
        val recentSongs: Flow<List<Song>>

        suspend fun insert(vararg recentSongs: RecentSong)
    }

    interface Remote {
        // todo
    }
}