package com.example.musicplayer.data.source

import com.example.musicplayer.data.model.artist.ArtistList
import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.artist.Artist
import com.example.musicplayer.data.model.artist.ArtistSongCrossRef
import com.example.musicplayer.data.model.artist.ArtistWithSongs
import kotlinx.coroutines.flow.Flow

interface ArtistDataSource {
    interface Local {
        val artists: Flow<List<Artist>>

        fun getArtistWithSongs(artistId: Int): ArtistWithSongs

        val top15Artists: Flow<List<Artist>>

        fun getArtistById(id: Int): Flow<Artist?>

        suspend fun insert(vararg artists: Artist)

        suspend fun insertArtistSongCrossRef(vararg artistSongCrossRef: ArtistSongCrossRef)

        suspend fun delete(artist: Artist)

        suspend fun update(artist: Artist)
    }

    interface Remote {
        suspend fun loadArtists(callback: ResultCallBack<Result<ArtistList>>)
    }
}