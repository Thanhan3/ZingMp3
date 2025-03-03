package com.example.musicplayer.data.model.playlist

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.musicplayer.data.model.song.Song

data class PlaylistWithSongs(
    @Embedded
    val playlist: Playlist? = null,

    @Relation(
        parentColumn = "playlist_id",
        entityColumn = "song_id",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    var songs: List<Song> = emptyList()
)