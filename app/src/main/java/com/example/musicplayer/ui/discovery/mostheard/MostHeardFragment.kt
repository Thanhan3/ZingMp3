package com.example.musicplayer.ui.discovery.mostheard

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentMostHeardBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.detail.DetailFragment
import com.example.musicplayer.ui.detail.DetailViewModel
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import com.example.musicplayer.utils.MusicAppUtils

class MostHeardFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentMostHeardBinding
    private lateinit var songAdapter: SongAdapter
    private val mostHeardViewModel: MostHeardViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        MostHeardViewModel.Factory(application.getSongRepository())
    }
    private val detailViewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMostHeardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        songAdapter = SongAdapter(
            object : SongAdapter.OnSongClickListener {
                override fun onSongClick(song: Song, position: Int) {
                    val playlistName = MusicAppUtils.DefaultPlaylistName.MOST_HEARD.value
                    playSong(song, position, playlistName)
                }

            },
            object : SongAdapter.OnSongOptionMenuClickListener {
                override fun onSongOptionMenuClick(position: Int) {
                    showOptionMenu(mostHeardViewModel.songs.value!![position])
                }

            }
        )
        binding.includeMostHeardSong.rvSongList.adapter = songAdapter
        binding.btnMoreMostHeard.setOnClickListener {
            navigateToDetailScreen()
        }
        binding.textTitleMostHeard.setOnClickListener {
            navigateToDetailScreen()
        }
    }

    private fun navigateToDetailScreen() {
        val playlistName = MusicAppUtils.DefaultPlaylistName.MOST_HEARD.value
        val screenName = getString(R.string.title_most_heard)
        detailViewModel.setScreenName(screenName)
        detailViewModel.setPlaylistName(playlistName)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, DetailFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    private fun observeData() {
        mostHeardViewModel.songs.observe(viewLifecycleOwner) { songs ->
            songAdapter.updateSongs(songs)
        }
        mostHeardViewModel.top40MostHeardSongs.observe(viewLifecycleOwner) { songs ->
            detailViewModel.setSongs(songs)
            val playlistName = MusicAppUtils.DefaultPlaylistName.MOST_HEARD.value
            SharedViewModel.instance!!.setupPlaylist(songs, playlistName)
        }
    }
}