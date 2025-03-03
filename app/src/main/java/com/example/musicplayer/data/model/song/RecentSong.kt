package com.example.musicplayer.data.model.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Date

@Entity(tableName = "recent_songs")
data class RecentSong(
    @ColumnInfo(name = "play_at")
    var playAt: Date = Date()
) : Song() {
    class Builder(song: Song) {
        private val recentSong: RecentSong = RecentSong().apply {
            id = song.id
            title = song.title
            artist = song.artist
            album = song.album
            duration = song.duration
            source = song.source
            image = song.image
            favorite = song.favorite
            counter = song.counter
            replay = song.replay
            playAt = Date()
        }

        fun build(): RecentSong {
            return recentSong
        }
    }
}