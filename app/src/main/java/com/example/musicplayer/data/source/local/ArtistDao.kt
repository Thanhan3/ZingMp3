package com.example.musicplayer.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.musicplayer.data.model.artist.Artist
import com.example.musicplayer.data.model.artist.ArtistSongCrossRef
import com.example.musicplayer.data.model.artist.ArtistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @get:Query("SELECT * FROM artists")
    val artists: Flow<List<Artist>>

    @get:Query("SELECT * FROM artists LIMIT 15")
    val top15Artists: Flow<List<Artist>>

    @Transaction
    @Query("SELECT * FROM artists WHERE artist_id = :artistId")
    fun getArtistWithSongs(artistId: Int): ArtistWithSongs

    @Query("SELECT * FROM artists WHERE artist_id = :id")
    fun getArtistById(id: Int): Flow<Artist?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg artists: Artist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistSongCrossRef(vararg artistSongCrossRef: ArtistSongCrossRef)

    @Delete
    suspend fun delete(artist: Artist)

    @Update
    suspend fun update(artist: Artist)
}