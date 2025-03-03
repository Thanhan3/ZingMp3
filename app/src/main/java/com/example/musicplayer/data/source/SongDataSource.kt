package com.example.musicplayer.data.source

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface SongDataSource {
    interface Local {
        val songs: List<Song>

        val favoriteSongs: Flow<List<Song>>

        val top15MostHeardSongs: Flow<List<Song>>

        val top40MostHeardSongs: Flow<List<Song>>

        val top15ForYouSongs: Flow<List<Song>>

        val top40ForYouSongs: Flow<List<Song>>

        suspend fun insert(vararg songs: Song)

        suspend fun delete(song: Song)

        suspend fun update(song: Song)

        suspend fun updateFavorite(id: String, favorite: Boolean)

        suspend fun getSongById(id: String): Song?
    }
    interface Remote{
        suspend fun loadSongs(callback: ResultCallBack<Result<List<Song>>>)
    }
}