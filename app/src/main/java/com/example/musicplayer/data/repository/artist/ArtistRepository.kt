package com.example.musicplayer.data.repository.artist

import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.artist.Artist
import com.example.musicplayer.data.model.artist.ArtistList
import com.example.musicplayer.data.model.artist.ArtistSongCrossRef
import com.example.musicplayer.data.model.artist.ArtistWithSongs
import com.example.musicplayer.data.source.Result
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    interface Local {
        val artists: Flow<List<Artist>>

        val top15Artists: Flow<List<Artist>>

        fun getArtistById(id: Int): Flow<Artist?>

        fun getArtistWithSongs(artistId: Int): ArtistWithSongs

        suspend fun insert(vararg artists: Artist)

        suspend fun insertArtistSongCrossRef(vararg artistSongCrossRef: ArtistSongCrossRef)

        suspend fun update(artist: Artist)

        suspend fun delete(artist: Artist)
    }

    interface Remote {
        suspend fun loadArtists(callback: ResultCallBack<Result<ArtistList>>)
    }
}