package com.example.musicplayer.data.repository.album

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.source.Result

interface AlbumRepository {
    interface  Local{

    }
    interface Remote{
        suspend fun loadAlbums(callback: ResultCallBack<Result<List<Album>>>)
    }
}