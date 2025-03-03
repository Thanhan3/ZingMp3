package com.example.musicplayer.data.source.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musicplayer.data.model.playlist.Playlist
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.data.model.artist.Artist
import com.example.musicplayer.data.model.artist.ArtistSongCrossRef
import com.example.musicplayer.data.model.playlist.PlaylistSongCrossRef
import com.example.musicplayer.data.model.song.RecentSong
import com.example.musicplayer.data.model.song.Song

@Database(
    entities = [
        Song::class,
        Playlist::class,
        Album::class,
        RecentSong::class,
        PlaylistSongCrossRef::class,
        Artist::class,
        ArtistSongCrossRef::class
    ],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 3, to = 4, spec = AppDatabase.MigrationSpec::class)
    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun albumDao(): AlbumDao
    abstract fun recentSongDao(): RecentSongDao
    abstract fun artistDao(): ArtistDao

    @RenameColumn(
        tableName = "artists",
        fromColumnName = "id",
        toColumnName = "artist_id"
    )
    internal class MigrationSpec : AutoMigrationSpec


    companion object {
        @Volatile
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (_instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (_instance == null) {
                        _instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "music.db"
                        )
                            .build()
                    }
                }
            }
            return _instance!!
        }
    }
}