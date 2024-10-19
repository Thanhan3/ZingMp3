package com.example.musicplayer.data.model.song

import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("album") val album: String = "",
    @SerializedName("artist") val artist: String = "",
    @SerializedName("source") val source: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("duration") val duration: Int = 0,
    @SerializedName("favorite") var favorite: Boolean = false,
    @SerializedName("counter") var counter: Int = 0,
    @SerializedName("replay") var replay: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Song) return false

        if (id != other.id) return false

        return true
    }
    override fun hashCode(): Int {
        return id.hashCode()
    }
}

