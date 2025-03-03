package com.example.musicplayer.data.model.artist

import com.google.gson.annotations.SerializedName

data class ArtistList(
    @SerializedName("artists") val artists: List<Artist>
)