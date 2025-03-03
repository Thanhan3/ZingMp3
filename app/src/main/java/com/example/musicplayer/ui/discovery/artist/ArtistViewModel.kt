package com.example.musicplayer.ui.discovery.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.artist.Artist

class ArtistViewModel : ViewModel() {
    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>>
        get() = _artists

    fun setArtists(artists: List<Artist>) {
        _artists.value = artists
    }
}