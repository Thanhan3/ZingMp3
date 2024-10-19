package com.example.musicplayer.data.repository.song

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.source.Result
import com.example.musicplayer.data.source.remote.RemoteSongDataSource

class SongRepositoryImpl : SongRepository.Remote , SongRepository.Local{
    override suspend fun loadSongs(callback: ResultCallBack<Result<List<Song>>>) {
        val remoteSongDataSource = RemoteSongDataSource()
        remoteSongDataSource.loadSongs(callback)
    }

}