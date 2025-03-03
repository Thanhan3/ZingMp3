package com.example.musicplayer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.ui.home.HomeViewModel
import com.example.musicplayer.ui.playing.PlaybackService
import com.example.musicplayer.ui.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var currentSongLoaded = false

    private val sharedViewModel: SharedViewModel by viewModels {
        val application = application as MusicApplication
        SharedViewModel.Factory(
            application.getSongRepository(),
            application.getRecentSongRepository()
        )
    }
    private val homeViewModel: HomeViewModel by viewModels {
        val application = application as MusicApplication
        HomeViewModel.Factory(application.getSongRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNav()
        setupComponents()
        observeData()
        sharedViewModel.initPlaylist()
    }

    override fun onStop() {
        super.onStop()
        saveCurrentSong()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, PlaybackService::class.java)) // Dừng Service ngay khi MainActivity bị hủy
    }

    private fun setupBottomNav(){
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment)
        val navController = navHostFragment.navController
        setupWithNavController(binding.navView, navController)
    }

    private fun setupComponents() {
        sharedViewModel.initPlaylist()
        sharedPreferences = getSharedPreferences(
            "com.example.musicplayer.preff_file",
            MODE_PRIVATE
        )
    }

    private fun observeData() {
        SharedViewModel.instance?.isReady?.observe(this) { ready ->
            if (ready && !currentSongLoaded) {
                loadPreviousSessionSong()
                currentSongLoaded = true
            }
        }
    }

    private fun saveCurrentSong() {
        val playingSong = SharedViewModel.instance?.playingSong?.value
        playingSong?.let {
            val song = it.song
            song?.let { currentSong ->
                sharedPreferences.edit()
                    .putString(PREF_SONG_ID, currentSong.id)
                    .putString(PREF_PLAYLIST_NAME, it.playlist?.name)
                    .apply()
            }
        }
    }

    private fun loadPreviousSessionSong() {
        val songId = sharedPreferences.getString(PREF_SONG_ID, null)
        val playlistName = sharedPreferences.getString(PREF_PLAYLIST_NAME, null)
        SharedViewModel.instance?.loadPreviousSessionSong(songId, playlistName)
    }

    companion object {
        const val PREF_SONG_ID = "PREF_SONG_ID"
        const val PREF_PLAYLIST_NAME = "PREF_PLAYLIST_NAME"
    }

}