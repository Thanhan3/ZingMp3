package com.example.musicplayer.ui.discovery.foryou

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
import com.example.musicplayer.databinding.FragmentForYouBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.detail.DetailFragment
import com.example.musicplayer.ui.detail.DetailViewModel
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import com.example.musicplayer.utils.MusicAppUtils

class ForYouFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentForYouBinding
    private lateinit var songAdapter: SongAdapter
    private val detailViewModel: DetailViewModel by activityViewModels()
    private val forYouViewModel: ForYouViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        ForYouViewModel.Factory(application.getSongRepository())
    }
    var songs = emptyList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForYouBinding.inflate(inflater, container, false)
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
                    val playlistName = MusicAppUtils.DefaultPlaylistName.FOR_YOU.value
                    playSong(song, position, playlistName)
                }

            },
            object : SongAdapter.OnSongOptionMenuClickListener {
                override fun onSongOptionMenuClick(position: Int) {
                    showOptionMenu(songs[position])
                }

            }
        )
        binding.btnMoreForYou.setOnClickListener {
            navigateToDetailScreen()
        }
        binding.textTitleForYou.setOnClickListener {
            navigateToDetailScreen()
        }
        binding.includeForYouSong.rvSongList.adapter = songAdapter
    }

    private fun navigateToDetailScreen() {
        val playlistName = MusicAppUtils.DefaultPlaylistName.FOR_YOU.value
        val screenName = getString(R.string.title_for_you)
        detailViewModel.setScreenName(screenName)
        detailViewModel.setPlaylistName(playlistName)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, DetailFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }


    private fun observeData() {
        forYouViewModel.songs.observe(viewLifecycleOwner) { songs ->
            songAdapter.updateSongs(songs)
        }
        forYouViewModel.top40Songs.observe(viewLifecycleOwner) { songs ->
            this.songs = songs
            val playlistName = MusicAppUtils.DefaultPlaylistName.FOR_YOU.value
            SharedViewModel.instance!!.setupPlaylist(songs, playlistName)
            detailViewModel.setSongs(songs)
        }
    }
}