package com.example.musicplayer.data.source

import com.example.musicplayer.data.model.search.HistorySearchedKey
import com.example.musicplayer.data.model.search.HistorySearchedSong
import com.example.musicplayer.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface SearchingDataSource {
    val allKeys: Flow<List<HistorySearchedKey>>

    val allSongs: Flow<List<HistorySearchedSong>>

    suspend fun search(key: String): Flow<List<Song>>

    suspend fun insertKey(keys: List<HistorySearchedKey>)

    suspend fun insertSong(songs: List<HistorySearchedSong>)

    suspend fun clearAllKeys()

    suspend fun clearAllSongs()
}