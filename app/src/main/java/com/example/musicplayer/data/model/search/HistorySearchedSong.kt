package com.example.musicplayer.data.model.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.musicplayer.data.model.song.Song
import java.util.Date

@Entity(tableName = "history_searched_songs")
data class HistorySearchedSong(
    @ColumnInfo(name = "selected_at") val selectedAt: Date
) : Song() {
    class Builder(private val song: Song) {
        private val historySearchedSong = HistorySearchedSong(Date())

        fun build(): HistorySearchedSong {
            historySearchedSong.id = song.id
            historySearchedSong.title = song.title
            historySearchedSong.artist = song.artist
            historySearchedSong.album = song.album
            historySearchedSong.duration = song.duration
            historySearchedSong.image = song.image
            historySearchedSong.counter = song.counter
            historySearchedSong.replay = song.replay
            historySearchedSong.source = song.source
            historySearchedSong.favorite = song.favorite
            return historySearchedSong
        }
    }
}