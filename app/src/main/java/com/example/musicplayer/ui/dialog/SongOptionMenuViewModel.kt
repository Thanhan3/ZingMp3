package com.example.musicplayer.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.utils.OptionMenuUtils

class SongOptionMenuViewModel : ViewModel() {
    private val _song = MutableLiveData<Song>()
    private val _optionMenuItems = MutableLiveData<List<MenuItem>>()

    val song: LiveData<Song>
        get() = _song
    val optionMenuItems: LiveData<List<MenuItem>>
        get() = _optionMenuItems

    init {
        _optionMenuItems.value = OptionMenuUtils.songOptionMenuItems
    }

    fun setSong(song: Song) {
        _song.value = song
    }
}