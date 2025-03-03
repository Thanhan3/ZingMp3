package com.example.musicplayer.utils

import android.content.Context
import com.example.musicplayer.data.repository.artist.ArtistRepositoryImpl
import com.example.musicplayer.data.repository.playlist.PlaylistRepositoryImpl
import com.example.musicplayer.data.repository.recent_song.RecentSongRepositoryImpl
import com.example.musicplayer.data.repository.song.SongRepositoryImpl
import com.example.musicplayer.data.source.ArtistDataSource
import com.example.musicplayer.data.source.PlaylistDataSource
import com.example.musicplayer.data.source.SongDataSource
import com.example.musicplayer.data.source.local.AppDatabase
import com.example.musicplayer.data.source.local.LocalArtistDataSource
import com.example.musicplayer.data.source.local.LocalPlaylistDataSource
import com.example.musicplayer.data.source.local.LocalRecentSongDataSource
import com.example.musicplayer.data.source.local.LocalSongDataSource

object InjectionUtils {
    fun provideRecentSongDataSource(
        context: Context
    ): LocalRecentSongDataSource {
        val database = AppDatabase.getInstance(context)
        return LocalRecentSongDataSource(database.recentSongDao())
    }

    fun provideRecentSongRepository(
        dataSource: LocalRecentSongDataSource
    ): RecentSongRepositoryImpl {
        return RecentSongRepositoryImpl(dataSource)
    }

    fun provideSongDataSource(context: Context): SongDataSource.Local {
        val database = AppDatabase.getInstance(context)
        return LocalSongDataSource(database.songDao())
    }

    fun provideSongRepository(
        dataSource: SongDataSource.Local
    ): SongRepositoryImpl {
        return SongRepositoryImpl(dataSource)
    }
    fun providePlaylistDataSource(context: Context): PlaylistDataSource.Local {
        val database = AppDatabase.getInstance(context)
        return LocalPlaylistDataSource(database.playlistDao())
    }

    fun providePlaylistRepository(dataSource: PlaylistDataSource.Local): PlaylistRepositoryImpl {
        return PlaylistRepositoryImpl(dataSource)
    }

    fun provideArtistDataSource(context: Context): ArtistDataSource.Local {
        val database = AppDatabase.getInstance(context)
        return LocalArtistDataSource(database.artistDao())
    }

    fun provideArtistRepository(dataSource: ArtistDataSource.Local): ArtistRepositoryImpl {
        return ArtistRepositoryImpl(dataSource)
    }

}