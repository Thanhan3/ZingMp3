package com.example.musicplayer.data.model.album

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("songs")
    val songs: List<String> = emptyList(),
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("artwork")
    val artwork: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Album) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}