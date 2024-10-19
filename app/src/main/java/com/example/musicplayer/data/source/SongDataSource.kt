package com.example.musicplayer.data.source

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.model.song.Song

interface SongDataSource {
    interface Local {

    }
    interface Remote{
        suspend fun loadSongs(callback: ResultCallBack<Result<List<Song>>>)
    }
}