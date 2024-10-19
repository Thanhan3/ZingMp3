package com.example.musicplayer.data.source.remote

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.source.AlbumDataSource
import com.example.musicplayer.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteAlbumDataSource: AlbumDataSource.Remote {
    override suspend fun loadAlbums(callback: ResultCallBack<Result<List<Album>>>) {
        withContext(Dispatchers.IO) {
            val response = RetrofitHelper.instance.loadAlbum()
            if (response.isSuccessful) {
                if(response.body() != null) {
                    val albums = response.body()!!.albums
                    callback.onResult(Result.Success(albums))
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