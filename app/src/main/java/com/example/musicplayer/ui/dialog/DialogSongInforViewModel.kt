package com.example.musicplayer.ui.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.song.Song

class DialogSongInforViewModel : ViewModel() {
    private val _song = MutableLiveData<Song>()

    val song: MutableLiveData<Song>
        get() = _song

    fun setSong(song: Song) {
        _song.value = song
    }
}