package com.example.musicplayer.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.data.model.search.HistorySearchedKey
import com.example.musicplayer.data.model.search.HistorySearchedSong
import com.example.musicplayer.data.model.song.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchingDao {
    @get:Query("SELECT * FROM history_searched_keys ORDER BY created_at DESC LIMIT 50")
    val allKeys: Flow<List<HistorySearchedKey>>

    @get:Query("SELECT * FROM history_searched_songs ORDER BY selected_at DESC LIMIT 100")
    val allSongs: Flow<List<HistorySearchedSong>>

    @Query("SELECT * FROM songs WHERE title LIKE :query OR artist LIKE :query")
    fun search(query: String): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<HistorySearchedKey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(songs: List<HistorySearchedSong>)

    @Query("DELETE FROM history_searched_keys")
    suspend fun clearAllKeys()

    @Query("DELETE FROM history_searched_songs")
    suspend fun clearAllSongs()
}