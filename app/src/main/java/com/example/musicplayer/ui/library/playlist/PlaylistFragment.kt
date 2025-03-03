package com.example.musicplayer.ui.library.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.playlist.Playlist
import com.example.musicplayer.databinding.FragmentPlaylistBinding
import com.example.musicplayer.ui.library.playlist.creation.DialogPlaylistCreationFragment
import com.example.musicplayer.ui.library.playlist.detail.PlaylistDetailFragment
import com.example.musicplayer.ui.library.playlist.detail.PlaylistDetailViewModel
import com.example.musicplayer.ui.library.playlist.more.MorePlaylistFragment
import com.example.musicplayer.ui.library.playlist.more.MorePlaylistViewModel
import com.example.musicplayer.ui.viewmodel.SharedViewModel

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var adapter: PlaylistAdapter
    private val playlistViewModel: PlaylistViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        PlaylistViewModel.Factory(application.getPlaylistRepository())
    }
    private val playlistDetailViewModel: PlaylistDetailViewModel by activityViewModels()
    private val morePlaylistViewModel: MorePlaylistViewModel by activityViewModels()
    private var shouldNavigateToDetail = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.btnMorePlaylist.setOnClickListener {
            navigateToMorePlaylist()
        }
        binding.textTitlePlaylist.setOnClickListener {
            navigateToMorePlaylist()
        }
        binding.includeButtonCreatePlaylist.btnItemCreatePlaylist.setOnClickListener {
            showDialogToCreatePlaylist()
        }
        binding.includeButtonCreatePlaylist.textItemCreatePlaylist.setOnClickListener {
            showDialogToCreatePlaylist()
        }
        adapter = PlaylistAdapter(
            object : PlaylistAdapter.OnPlaylistClickListener {
                override fun onPlaylistClick(playlist: Playlist) {
                    playlistViewModel.getPlaylistWithSongByPlaylistId(playlist.id)
                    shouldNavigateToDetail = true
                }

                override fun onPlaylistMenuOptionClick(playlist: Playlist) {
                    // todo
                }
            }
        )
        binding.rvPlaylist.adapter = adapter
    }

    private fun navigateToMorePlaylist() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, MorePlaylistFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToPlaylistDetail() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, PlaylistDetailFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    private fun showDialogToCreatePlaylist() {
        val listener = object : DialogPlaylistCreationFragment.OnClickListener {
            override fun onClick(playlistName: String) {
                playlistViewModel.createNewPlaylist(playlistName)
            }
        }
        val textChangeListener = object : DialogPlaylistCreationFragment.OnTextChangeListener {
            override fun onTextChange(playlistName: String) {
                playlistViewModel.findPlaylistByName(playlistName)
            }
        }
        val dialog = DialogPlaylistCreationFragment(listener,textChangeListener)
        val tag = DialogPlaylistCreationFragment.TAG
        dialog.show(requireActivity().supportFragmentManager, tag)
    }

    private fun observeData() {
        playlistViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            adapter.updatePlaylists(playlists)
        }
        playlistViewModel.playlists.observe(viewLifecycleOwner) {
            morePlaylistViewModel.setPlaylists(it)
        }
        playlistViewModel.playlistWithSongs.observe(viewLifecycleOwner) { playlistWithSongs ->
            if(shouldNavigateToDetail) {
                playlistWithSongs.playlist?.let {
                    it.updateSongList(playlistWithSongs.songs)
                    SharedViewModel.instance!!.addPlaylist(it.name)
                    SharedViewModel.instance?.setupPlaylist(it.songs, it.name)
                }
                playlistDetailViewModel.setPlaylistWithSongs(playlistWithSongs)
                navigateToPlaylistDetail()
                shouldNavigateToDetail = false
            }
        }
    }
}