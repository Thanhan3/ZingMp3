package com.example.musicplayer.data.source.remote

import com.example.musicplayer.data.model.album.AlbumList
import com.example.musicplayer.data.model.song.Song
import retrofit2.Response
import retrofit2.http.GET
import com.example.musicplayer.data.model.song.SongList

interface MusicService {
    /* api cho danh sách bài hát
       api cho danh sách album
       api cho danh dách ca sĩ
       https://thantrieu.com/resources/braniumapis/playlist.json
       */

    @GET("/resources/braniumapis/songs.json")
    suspend fun loadSong () : Response<SongList>
    @GET("/resources/braniumapis/playlist.json")
    suspend fun loadAlbum() : Response<AlbumList>
}