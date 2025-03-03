package com.example.musicplayer.data.repository.searching

import com.example.musicplayer.data.model.search.HistorySearchedKey
import com.example.musicplayer.data.model.search.HistorySearchedSong
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.source.SearchingDataSource
import kotlinx.coroutines.flow.Flow

class SearchingRepositoryImpl(
    private val searchingDataSource: SearchingDataSource
) : SearchingRepository {
    override val allKeys: Flow<List<HistorySearchedKey>>
        get() = searchingDataSource.allKeys

    override val allSongs: Flow<List<HistorySearchedSong>>
        get() = searchingDataSource.allSongs

    override suspend fun search(key: String): Flow<List<Song>> {
        return searchingDataSource.search(key)
    }

    override suspend fun insertKey(keys: List<HistorySearchedKey>) {
        searchingDataSource.insertKey(keys)
    }

    override suspend fun insertSong(songs: List<HistorySearchedSong>) {
        searchingDataSource.insertSong(songs)
    }

    override suspend fun clearAllKeys() {
        searchingDataSource.clearAllKeys()
    }

    override suspend fun clearAllSongs() {
        searchingDataSource.clearAllSongs()
    }
}