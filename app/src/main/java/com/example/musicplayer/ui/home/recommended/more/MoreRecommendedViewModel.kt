package com.example.musicplayer.ui.home.recommended.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.song.Song

class MoreRecommendedViewModel :ViewModel(){
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>>
        get() = _songs
    fun setSongs(songs:List<Song>){
        _songs.value = songs
    }
}