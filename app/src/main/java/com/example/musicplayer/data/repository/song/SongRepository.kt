package com.example.musicplayer.data.repository.song

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.data.source.Result

interface SongRepository {
    interface  Local{

    }
    interface Remote{
        suspend fun loadSongs(callback: ResultCallBack<Result<List<Song>>>)
    }
}