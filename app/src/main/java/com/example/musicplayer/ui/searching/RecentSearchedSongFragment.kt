package com.example.musicplayer.ui.searching

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentRecentSearchedSongBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.dialog.ConfirmationDialogFragment
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import com.example.musicplayer.utils.MusicAppUtils

class RecentSearchedSongFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentRecentSearchedSongBinding
    private lateinit var songAdapter: SongAdapter


    private val viewModel: SearchingViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        SearchingViewModel.Factory(application.getSearchRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentSearchedSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        songAdapter = SongAdapter(
            object : SongAdapter.OnSongClickListener {
                override fun onSongClick(song: Song, position: Int) {
                    viewModel.insertSelectedSong(song)
                    play(song)
                }

            },
            object : SongAdapter.OnSongOptionMenuClickListener {
                override fun onSongOptionMenuClick(position: Int) {
                    showOptionMenu(viewModel.songs.value!![position])
                }

            }
        )
        binding.rvHistorySong.adapter = songAdapter
        binding.tvClearSongHistory.setOnClickListener {
            val message = R.string.message_confirm_clear_song_history
            val dialog = ConfirmationDialogFragment(message,
                object : ConfirmationDialogFragment.OnDeleteConfirmListener {
                    override fun onConfirm(isConfirmed: Boolean) {
                        if (isConfirmed) {
                            viewModel.clearSearchedSongsHistory()
                        }
                    }
                })
            dialog.show(requireActivity().supportFragmentManager, ConfirmationDialogFragment.TAG)
        }
    }

    private fun play(song: Song) {
        val playlistName = MusicAppUtils.DefaultPlaylistName.SEARCH.value
        playSong(song, 0, playlistName)
    }

    private fun setupObserver() {
        viewModel.historySearchedSongs.observe(viewLifecycleOwner) { songs ->
            songAdapter.updateSongs(songs)
            SharedViewModel.instance!!.setupPlaylist(songs, MusicAppUtils.DefaultPlaylistName.SEARCH.value)
        }
    }
}