package com.example.musicplayer.ui.library.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentFavoriteBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.detail.DetailFragment
import com.example.musicplayer.ui.detail.DetailViewModel
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.utils.MusicAppUtils

class FavoriteFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: SongAdapter
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.textTitleFavorite.setOnClickListener {
            navigateToDetailScreen()
        }
        binding.btnMoreFavorite.setOnClickListener {
            navigateToDetailScreen()
        }
        adapter = SongAdapter(
            object : SongAdapter.OnSongClickListener {
                override fun onSongClick(song: Song, position: Int) {
                    playSong(song,position, MusicAppUtils.DefaultPlaylistName.FAVORITES.value)
                }

            },
            object : SongAdapter.OnSongOptionMenuClickListener {
                override fun onSongOptionMenuClick(position: Int) {
                   // TODO("Not yet implemented")
                }
            }
        )
        binding.includeFavorite.rvSongList.adapter = adapter
    }

    private fun navigateToDetailScreen() {
        val playlistName = MusicAppUtils.DefaultPlaylistName.FAVORITES.value
        val screenName = getString(R.string.title_favorite)
        detailViewModel.setScreenName(screenName)
        detailViewModel.setPlaylistName(playlistName)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, DetailFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    private fun observeData() {
        favoriteViewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs)
            detailViewModel.setSongs(songs)
        }
    }
}