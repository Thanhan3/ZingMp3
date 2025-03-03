package com.example.musicplayer.ui.discovery


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.ResultCallBack
import com.example.musicplayer.data.source.Result
import com.example.musicplayer.data.model.artist.Artist
import com.example.musicplayer.data.model.artist.ArtistList
import com.example.musicplayer.data.model.artist.ArtistSongCrossRef
import com.example.musicplayer.data.repository.artist.ArtistRepositoryImpl
import com.example.musicplayer.data.repository.song.SongRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoveryViewModel(
    private val artistRepository: ArtistRepositoryImpl,
    private val songRepository: SongRepositoryImpl
) : ViewModel() {
    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>>
        get() = _artists

    private val localArtists = artistRepository.artists.asLiveData()
    val top15Artists = artistRepository.top15Artists.asLiveData()
    val top15MostHeardSongs = songRepository.top15MostHeardSongs.asLiveData()
    val top15ForYouSongs = songRepository.top15ForYouSongs.asLiveData()

    init {
        loadArtists()
    }

    fun saveArtistToDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val artists = extractArtistsNotInDB()
            val artistToInsert = artists.toTypedArray()
            artistRepository.insert(*artistToInsert)
        }
    }

    fun saveArtistSongCrossRef(artists: List<Artist>) {
        viewModelScope.launch(Dispatchers.IO) {
            val localSongs = songRepository.songs
            val crossRefs = mutableListOf<ArtistSongCrossRef>()
            if (artists.isNotEmpty()) {
                for (artist in artists) {
                    for (song in localSongs) {
                        val key = ".*" + artist.name.lowercase().replace(" ", "") + ".*"
                        if (song.artist.lowercase().replace(" ", "").matches(key.toRegex())) {
                            crossRefs.add(ArtistSongCrossRef(song.id, artist.id))
                        }
                    }
                }
            }
            val crossRefToInsert = crossRefs.toTypedArray()
            artistRepository.insertArtistSongCrossRef(*crossRefToInsert)
        }
    }


    private fun extractArtistsNotInDB(): List<Artist> {
        val results = mutableListOf<Artist>()
        val localArtistList = localArtists.value ?: emptyList()
        val remoteArtistList = artists.value ?: emptyList()
        if (remoteArtistList.isNotEmpty()) {
            if (localArtistList.isNotEmpty()) {
                results.addAll(remoteArtistList)
            } else {
                for (artist in remoteArtistList) {
                    if (!localArtistList.contains(artist)) {
                        results.add(artist)
                    }
                }
            }
        }
        return results
    }

    private fun loadArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.loadArtists(object : ResultCallBack<Result<ArtistList>> {
                override fun onResult(result: Result<ArtistList>) {
                    if (result is Result.Success) {
                        _artists.postValue(result.data.artists)
                    } else {
                        _artists.postValue(emptyList())
                    }
                }
            })
        }
    }

    class Factory(
        private val artistRepository: ArtistRepositoryImpl,
        private val songRepository: SongRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiscoveryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DiscoveryViewModel(artistRepository,songRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}