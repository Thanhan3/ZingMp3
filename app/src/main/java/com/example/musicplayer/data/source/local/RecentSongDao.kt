package com.example.musicplayer.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.data.model.song.RecentSong
import com.example.musicplayer.data.model.song.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSongDao {
    @get:Query("SELECT * FROM recent_songs ORDER BY play_at DESC LIMIT 30")
    val recentSongs: Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg songs: RecentSong)
}