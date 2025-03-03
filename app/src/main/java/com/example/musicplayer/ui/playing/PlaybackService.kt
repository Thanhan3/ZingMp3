package com.example.musicplayer.ui.playing

import android.app.PendingIntent
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.ui.playing.nowplaying.NowPlayingActivity
import com.example.musicplayer.ui.viewmodel.SharedViewModel

class PlaybackService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession
    private lateinit var listener: Player.Listener
    private val handler = Handler(Looper.getMainLooper())
    private var recentSongRunnable: Runnable? = null

    private val singleTopActivity: PendingIntent
        get() {
            val intent = Intent(applicationContext, NowPlayingActivity::class.java)
            return PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onCreate() {
        super.onCreate()
        initSessionAndPlayer()
        setupListener()
    }

    override fun onDestroy() {
        mediaSession.player.removeListener(listener)
        mediaSession.player.release()
        mediaSession.release()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf() // Dừng Service
        mediaSession.player.release() // Giải phóng tài nguyên
        mediaSession.release()
        super.onTaskRemoved(rootIntent)
    }

    private fun initSessionAndPlayer() {
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()
        val builder = MediaSession.Builder(this, player)
        val intent = singleTopActivity
        builder.setSessionActivity(intent)
        mediaSession = builder.build()
    }

    private fun setupListener() {
        val player = mediaSession.player
        listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                cancelRecentSongTimer()

                val playlistChanged =
                    reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED
                val indexToPlay = SharedViewModel.instance?.indexToPlay?.value ?: 0

                if (!playlistChanged || indexToPlay == 0) {
                    // Cập nhật bài hát đang phát hiện tại
                    SharedViewModel.instance?.setPlayingSong(player.currentMediaItemIndex)
                    // Bắt đầu timer cho bài hát mới
                    startRecentSongTimer(extractSong())
                }
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED || playbackState == Player.STATE_IDLE) {
                    stopSelf() // Dừng Service khi không còn bài hát nào đang phát
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                stopSelf() // Dừng Service khi có lỗi xảy ra
            }

        }
        player.addListener(listener)
    }


    private fun startRecentSongTimer(song: Song?) {
        if (song == null) return
        recentSongRunnable = Runnable {
            // Kiểm tra nếu sau 5 giây bài hát vẫn đang phát thì insert vào DB
            val player = mediaSession.player
            if (player.isPlaying) {
                SharedViewModel.instance?.insertRecentSongToDB(song)
                saveCounterAndReplay()
            }
        }
        handler.postDelayed(recentSongRunnable!!, 5000) // Chờ 5 giây
    }

    private fun cancelRecentSongTimer() {
        recentSongRunnable?.let { handler.removeCallbacks(it) }
        recentSongRunnable = null
    }

    private fun saveCounterAndReplay() {
        val song = extractSong()
        song?.let {
            val handlerThread = HandlerThread(
                "UpdateCounterAndReplayThread",
                Process.THREAD_PRIORITY_BACKGROUND
            )
            handlerThread.start()
            val handler = Handler(handlerThread.looper)
            handler.post {
                SharedViewModel.instance?.updateSongInDB(song)
            }
        }
    }

    private fun extractSong(): Song? {
        return SharedViewModel.instance?.playingSong?.value?.song
    }
}