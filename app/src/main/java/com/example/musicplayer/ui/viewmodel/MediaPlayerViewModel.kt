package com.example.musicplayer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController

class MediaPlayerViewModel private constructor() : ViewModel() {
    private val _mediaController = MutableLiveData<MediaController>()
    val mediaController: MutableLiveData<MediaController> = _mediaController

    fun setMediaController(controller: MediaController) {
        _mediaController.value = controller
    }

    companion object {
        val instance: MediaPlayerViewModel = MediaPlayerViewModel()
    }
}