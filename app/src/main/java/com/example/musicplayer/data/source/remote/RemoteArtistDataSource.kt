package com.example.musicplayer.data.source.remote

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.artist.ArtistList
import com.example.musicplayer.data.source.ArtistDataSource
import com.example.musicplayer.data.source.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteArtistDataSource : ArtistDataSource.Remote {

    override suspend fun loadArtists(callback: ResultCallBack<Result<ArtistList>>) {
        withContext(Dispatchers.IO) {
            val response = RetrofitHelper.instance.loadArtists()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val artistList = response.body()!!
                    callback.onResult(Result.Success(artistList))
                } else {
                    callback.onResult(Result.Error(Exception("Empty response")))
                }
            } else {
                callback.onResult(Result.Error(Exception(response.message())))
            }
        }
    }

}