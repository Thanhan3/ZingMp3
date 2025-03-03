package com.example.musicplayer.ui.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentDetailBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.ui.viewmodel.SharedViewModel

class DetailFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var adapter: SongAdapter
    private val detailViewModel: DetailViewModel by activityViewModels()
    var playlistName = ""
    var song = emptyList<Song>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.toolbarDetailSongList.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        adapter = SongAdapter(
            object : SongAdapter.OnSongClickListener {
                override fun onSongClick(song: Song, position: Int) {
                    playSong(song, position, playlistName)
                }

            },
            object : SongAdapter.OnSongOptionMenuClickListener {
                override fun onSongOptionMenuClick(position: Int) {
                    showOptionMenu(song[position])
                }

            }
        )
        binding.includeSongList.rvSongList.adapter = adapter
    }

    private fun observeData() {
        detailViewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs)
            song = songs
        }
        detailViewModel.screenName.observe(viewLifecycleOwner) { screenName ->
            binding.textTitleDetailSongList.text = screenName
        }
        detailViewModel.playlistName.observe(viewLifecycleOwner) { playlistName ->
            this.playlistName = playlistName
        }
    }
}