package com.example.musicplayer.data.model.song

import com.google.gson.annotations.SerializedName

data class SongList(
    @SerializedName("songs")
    val songs: List<Song> = emptyList()
)
// https://thantrieu.com/resources/braniumapis/songs.json