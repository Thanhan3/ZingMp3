package com.example.musicplayer.utils

object MusicAppUtils {
    enum class DefaultPlaylistName(val value: String) {
        DEFAULT("Default"),
        FAVORITES("Favorites"),
        RECOMMENDED("Recommended"),
        RECENT("Recent"),
        SEARCH("Search"),
        MOST_HEARD("Most_Heard"),
        FOR_YOU("For_You"),
        CUSTOM("Custom")
    }
    const val EXTRA_CURRENT_FRACTION = "EXTRA_CURRENT_FRACTION"
}