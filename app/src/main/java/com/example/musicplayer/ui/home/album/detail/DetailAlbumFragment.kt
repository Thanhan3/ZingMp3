package com.example.musicplayer.ui.home.album.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentDetailAlbumBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import com.example.musicplayer.utils.MusicAppUtils

class DetailAlbumFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentDetailAlbumBinding
    private lateinit var adapter: SongAdapter
    private val detailAlbumViewModel: DetailAlbumViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
        setupPlaylist()
    }

    private fun setupPlaylist() {
        val playlistName = detailAlbumViewModel.album.value?.name
        val songs = detailAlbumViewModel.songs.value
        SharedViewModel.instance?.addPlaylist(playlistName!!)
        SharedViewModel.instance?.setupPlaylist(songs, playlistName!!)
    }

    private fun setupView() {
        binding.includeAlbumDetail.toolbarPlaylistDetail.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        adapter = SongAdapter(
            object : SongAdapter.OnSongClickListener {
                override fun onSongClick(song: Song, position: Int) {
                    val playlistName = detailAlbumViewModel.album.value?.name
                    playSong(song, position, playlistName!!)
                }
            },
            object : SongAdapter.OnSongOptionMenuClickListener {
                override fun onSongOptionMenuClick(position: Int) {
                    showOptionMenu(detailAlbumViewModel.songs.value!![position])
                }
            }
        )
        binding.includeAlbumDetail.includeSongList.rvSongList.adapter = adapter
    }

    private fun setupViewModel() {
        detailAlbumViewModel.album.observe(viewLifecycleOwner) { album ->
            binding.includeAlbumDetail.textPlaylistDetailTitle.text = album.name
            val text = getString(R.string.text_playlist_num_of_song, album.size)
            binding.includeAlbumDetail.textPlaylistDetailNumOfSong.text = text
            Glide.with(binding.root)
                .load(album.artwork)
                .error(R.drawable.ic_album)
                .into(binding.includeAlbumDetail.imagePlaylistArtwork)
        }
        detailAlbumViewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs)
        }
    }
}