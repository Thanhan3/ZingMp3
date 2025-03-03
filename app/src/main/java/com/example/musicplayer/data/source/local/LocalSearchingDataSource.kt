package com.example.musicplayer.data.source.local

import com.example.musicplayer.data.model.search.HistorySearchedKey
import com.example.musicplayer.data.model.search.HistorySearchedSong
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.source.SearchingDataSource
import kotlinx.coroutines.flow.Flow

class LocalSearchingDataSource(
    private val searchingDao: SearchingDao
) : SearchingDataSource {
    override val allKeys: Flow<List<HistorySearchedKey>>
        get() = searchingDao.allKeys

    override val allSongs: Flow<List<HistorySearchedSong>>
        get() = searchingDao.allSongs

    override suspend fun search(key: String): Flow<List<Song>> {
        val keyToSearch = "%${key}%"
        return searchingDao.search(keyToSearch)
    }

    override suspend fun insertKey(keys: List<HistorySearchedKey>) {
        searchingDao.insert(keys)
    }

    override suspend fun insertSong(songs: List<HistorySearchedSong>) {
        searchingDao.insertSong(songs)
    }

    override suspend fun clearAllKeys() {
        searchingDao.clearAllKeys()
    }

    override suspend fun clearAllSongs() {
        searchingDao.clearAllSongs()
    }
}