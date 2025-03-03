package com.example.musicplayer.data.repository.song

import androidx.room.Query
import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.source.Result
import com.example.musicplayer.data.source.SongDataSource
import com.example.musicplayer.data.source.remote.RemoteSongDataSource
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl(
    private val localSongDataSource: SongDataSource.Local
) : SongRepository.Remote , SongRepository.Local{
    override suspend fun loadSongs(callback: ResultCallBack<Result<List<Song>>>) {
        val remoteSongDataSource = RemoteSongDataSource()
        remoteSongDataSource.loadSongs(callback)
    }

    override val songs: List<Song>
        get() = localSongDataSource.songs

    override val favoriteSongs: Flow<List<Song>>
        get() = localSongDataSource.favoriteSongs

    override val top15MostHeardSongs: Flow<List<Song>>
        get() = localSongDataSource.top15MostHeardSongs

    override val top40MostHeardSongs: Flow<List<Song>>
        get() = localSongDataSource.top40MostHeardSongs

    override val top15ForYouSongs: Flow<List<Song>>
        get() = localSongDataSource.top15ForYouSongs

    override val top40ForYouSongs: Flow<List<Song>>
        get() = localSongDataSource.top40ForYouSongs

    override suspend fun insert(vararg songs: Song) {
        localSongDataSource.insert(*songs)
    }

    override suspend fun delete(song: Song) {
        localSongDataSource.delete(song)
    }

    override suspend fun update(song: Song) {
        localSongDataSource.update(song)
    }

    override suspend fun updateFavorite(id: String, favorite: Boolean) {
        localSongDataSource.updateFavorite(id, favorite)
    }

    override suspend fun getSongById(id: String): Song? {
       return localSongDataSource.getSongById(id)
    }

}