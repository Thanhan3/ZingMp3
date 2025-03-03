package com.example.musicplayer.data.repository.artist


import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.model.artist.Artist
import com.example.musicplayer.data.model.artist.ArtistList
import com.example.musicplayer.data.model.artist.ArtistSongCrossRef
import com.example.musicplayer.data.model.artist.ArtistWithSongs
import com.example.musicplayer.data.source.ArtistDataSource
import com.example.musicplayer.data.source.Result
import com.example.musicplayer.data.source.remote.RemoteArtistDataSource
import kotlinx.coroutines.flow.Flow

class ArtistRepositoryImpl(
    private val localDataSource: ArtistDataSource.Local
) : ArtistRepository.Remote, ArtistRepository.Local {
    private final val remoteArtistDataSource = RemoteArtistDataSource()

    override val artists: Flow<List<Artist>>
        get() = localDataSource.artists

    override val top15Artists: Flow<List<Artist>>
        get() = localDataSource.top15Artists

    override fun getArtistWithSongs(artistId: Int): ArtistWithSongs {
        return localDataSource.getArtistWithSongs(artistId)
    }

    override fun getArtistById(id: Int): Flow<Artist?> {
        return localDataSource.getArtistById(id)
    }

    override suspend fun insert(vararg artists: Artist) {
        localDataSource.insert(*artists)
    }

    override suspend fun insertArtistSongCrossRef(vararg artistSongCrossRef: ArtistSongCrossRef) {
        localDataSource.insertArtistSongCrossRef(*artistSongCrossRef)
    }

    override suspend fun update(artist: Artist) {
        localDataSource.update(artist)
    }

    override suspend fun delete(artist: Artist) {
        localDataSource.delete(artist)
    }

    override suspend fun loadArtists(callback: ResultCallBack<Result<ArtistList>>) {
        remoteArtistDataSource.loadArtists(callback)
    }
}