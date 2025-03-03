package com.example.musicplayer.ui.playing.nowplaying

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.C
import androidx.media3.session.MediaController
import java.util.Locale

class NowPlayingViewModel : ViewModel() {
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    fun setIsPlaying(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    fun getDuration(mediaController: MediaController?): Int {
        if (mediaController == null) return 0
        val maxDuration = mediaController.duration
        if (maxDuration == C.TIME_UNSET) {
            return 300 * 1000
        }
        return maxDuration.toInt()
    }

    fun getTimeLabel(value: Long): String {
        val minute = value / 60000
        val second = (value / 1000) % 60
        return if (value < 0 || value > Int.MAX_VALUE) "00:00"
        else String.format(Locale.ENGLISH, "%02d:%02d", minute, second)
    }
}