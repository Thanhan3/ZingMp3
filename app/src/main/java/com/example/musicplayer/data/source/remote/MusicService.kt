package com.example.musicplayer.data.source.remote

import com.example.musicplayer.data.model.album.AlbumList
import com.example.musicplayer.data.model.artist.ArtistList
import com.example.musicplayer.data.model.song.Song
import retrofit2.Response
import retrofit2.http.GET
import com.example.musicplayer.data.model.song.SongList

interface MusicService {
    @GET("/resources/braniumapis/songs.json")
    suspend fun loadSong () : Response<SongList>
    @GET("/resources/braniumapis/playlist.json")
    suspend fun loadAlbum() : Response<AlbumList>
    @GET("resources/braniumapis/artists.json")
    suspend fun loadArtists(): Response<ArtistList>
}