package com.example.musicplayer.data.repository.album

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.source.Result
import com.example.musicplayer.data.source.remote.RemoteAlbumDataSource

class AlbumRepositoryImpl: AlbumRepository.Remote , AlbumRepository.Local {
    private val remoteAlbumDataSource = RemoteAlbumDataSource()
    override suspend fun loadAlbums(callback: ResultCallBack<Result<List<Album>>>) {
        remoteAlbumDataSource.loadAlbums(callback)
    }
}