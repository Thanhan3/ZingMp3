package com.example.musicplayer.ui.library

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentLibraryBinding
import com.example.musicplayer.ui.library.favorite.FavoriteViewModel
import com.example.musicplayer.ui.library.playlist.PlaylistViewModel
import com.example.musicplayer.ui.library.recent.RecentViewModel
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import com.example.musicplayer.utils.MusicAppUtils

class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private val libraryViewModel: LibraryViewModel by viewModels {
        val application = requireActivity().application as MusicApplication
        LibraryViewModel.Factory(
            application.getRecentSongRepository(),
            application.getSongRepository(),
            application.getPlaylistRepository()
        )
    }
    private val playlistViewModel: PlaylistViewModel by activityViewModels  {
        val application = requireActivity().application as MusicApplication
        PlaylistViewModel.Factory(application.getPlaylistRepository())
    }
    private val recentSongViewModel: RecentViewModel by activityViewModels()
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        libraryViewModel.recentSongs.observe(viewLifecycleOwner) { recentSongs ->
            recentSongViewModel.setRecentSongs(recentSongs)
            SharedViewModel.instance!!.setupPlaylist(recentSongs,
                MusicAppUtils.DefaultPlaylistName.RECENT.value)
        }
        libraryViewModel.favoriteSongs.observe(viewLifecycleOwner) { favoriteSongs ->
            SharedViewModel.instance!!.setupPlaylist(favoriteSongs,
                MusicAppUtils.DefaultPlaylistName.FAVORITES.value)
            favoriteViewModel.setSongs(favoriteSongs)
        }
    }
}