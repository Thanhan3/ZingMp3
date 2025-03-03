package com.example.musicplayer.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.utils.OptionMenuUtils

class SongOptionMenuViewModel : ViewModel() {
    private val _song = MutableLiveData<Song>()
    private val _optionMenuItems = MutableLiveData<List<MenuItem>>()
    private val _playlistName = MutableLiveData<String>()

    val song: LiveData<Song>
        get() = _song
    val optionMenuItems: LiveData<List<MenuItem>>
        get() = _optionMenuItems
    val playlistName: MutableLiveData<String> = _playlistName

    init {
        _optionMenuItems.value = OptionMenuUtils.songOptionMenuItems
    }

    fun setPlaylistName(name: String) {
        _playlistName.value = name
    }

    fun setSong(song: Song) {
        _song.value = song
    }
}