package com.example.musicplayer

import android.app.Application
import android.content.ComponentName
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicplayer.data.repository.artist.ArtistRepositoryImpl
import com.example.musicplayer.data.repository.playlist.PlaylistRepositoryImpl
import com.example.musicplayer.data.repository.recent_song.RecentSongRepositoryImpl
import com.example.musicplayer.data.repository.searching.SearchingRepositoryImpl
import com.example.musicplayer.data.repository.song.SongRepositoryImpl
import com.example.musicplayer.ui.playing.PlaybackService
import com.example.musicplayer.ui.viewmodel.MediaPlayerViewModel
import com.example.musicplayer.utils.InjectionUtils
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.ExecutionException

class MusicApplication : Application() {
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private var mediaController: MediaController? = null
    private lateinit var recentSongRepository: RecentSongRepositoryImpl
    private lateinit var songRepository: SongRepositoryImpl
    private lateinit var playlistRepository: PlaylistRepositoryImpl
    private lateinit var artistRepository: ArtistRepositoryImpl
    private lateinit var searchRepository: SearchingRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        createMediaPlayer()
        setupComponents()
    }

    private fun createMediaPlayer() {
        val sessionToken = SessionToken(
            applicationContext,
            ComponentName(applicationContext, PlaybackService::class.java)
        )
        controllerFuture = MediaController.Builder(applicationContext, sessionToken).buildAsync()
        controllerFuture.addListener({
            if (controllerFuture.isDone && !controllerFuture.isCancelled) {
                try {
                    mediaController = controllerFuture.get()
                    mediaController?.let {
                        MediaPlayerViewModel.instance.setMediaController(it)
                    }
                } catch (ignored: ExecutionException) {
                } catch (ignored: InterruptedException) {
                }
            } else {
                mediaController = null
            }
        }, MoreExecutors.directExecutor())
    }

    private fun setupComponents() {

        val artistDataSource = InjectionUtils.provideArtistDataSource(applicationContext)
        artistRepository = InjectionUtils.provideArtistRepository(artistDataSource)

        val songDataSource =
            InjectionUtils.provideSongDataSource(applicationContext)
        songRepository = InjectionUtils.provideSongRepository(songDataSource)

        val recentSongDataSource =
            InjectionUtils.provideRecentSongDataSource(applicationContext)
        recentSongRepository = InjectionUtils.provideRecentSongRepository(recentSongDataSource)

        val playlistDataSource =
            InjectionUtils.providePlaylistDataSource(applicationContext)
        playlistRepository= InjectionUtils.providePlaylistRepository(playlistDataSource)

        val searchingDataSource =
            InjectionUtils.provideSearchingDataSource(applicationContext)
        searchRepository = InjectionUtils.provideSearchingRepository(searchingDataSource)
    }

    fun getArtistRepository(): ArtistRepositoryImpl {
        return artistRepository
    }

    fun getRecentSongRepository(): RecentSongRepositoryImpl {
        return recentSongRepository
    }

    fun getSongRepository(): SongRepositoryImpl {
        return songRepository
    }

    fun getPlaylistRepository():PlaylistRepositoryImpl{
        return playlistRepository
    }

    fun getSearchRepository(): SearchingRepositoryImpl {
        return searchRepository
    }
}