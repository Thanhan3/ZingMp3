package com.example.musicplayer.data.source.remote

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.source.Result
import com.example.musicplayer.data.source.SongDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteSongDataSource: SongDataSource.Remote {
    override suspend fun loadSongs(callback: ResultCallBack<Result<List<Song>>>) {
        withContext(Dispatchers.IO) {
            val response = RetrofitHelper.instance.loadSong()
            if (response.isSuccessful) {
                if(response.body() != null) {
                    val songs = response.body()!!.songs
                    callback.onResult(Result.Success(songs))
                }
                else{
                    callback.onResult(Result.Error(Exception("Empty response")))
                }

            }
            else{
                callback.onResult(Result.Error(Exception(response.message())))
            }
        }
    }

}
