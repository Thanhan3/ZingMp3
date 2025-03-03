package com.example.musicplayer.ui.discovery.artist.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.artist.Artist
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentArtistDetailBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.ui.viewmodel.SharedViewModel

class ArtistDetailFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentArtistDetailBinding
    private lateinit var adapter: SongAdapter
    var playlistName = ""
    var songs = emptyList<Song>()
    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        ArtistDetailViewModel.Factory(application.getArtistRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.toolbarArtistDetail.setNavigationOnClickListener {
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
                    showOptionMenu(songs[position])
                }

            }
        )
        binding.includeDetailArtistSongList.rvSongList.adapter = adapter
    }

    private fun observeData() {
        artistDetailViewModel.artistWithSongs.observe(viewLifecycleOwner) { artistWithSongs ->
            adapter.updateSongs(artistWithSongs.songs)
            this.songs = artistWithSongs.songs
            SharedViewModel.instance!!.setupPlaylist(artistWithSongs.songs, playlistName)
        }
        artistDetailViewModel.artist.observe(viewLifecycleOwner) { artist ->
            showArtistInfo(artist)
            this.playlistName = artist.name
            SharedViewModel.instance!!.addPlaylist(playlistName)
        }
    }

    private fun showArtistInfo(artist: Artist) {
        binding.textDetailArtistName.text = getString(R.string.text_artist_name, artist.name)
        binding.textDetailInterested.text =
            getString(R.string.text_number_subscriber, artist.interested)
        val interested = if (artist.isCareAbout) "YES" else "NO"
        binding.textArtistDetailYourInterest.text = getString(R.string.text_artist_name, interested)
        Glide.with(binding.root)
            .load(artist.avatar)
            .error(R.drawable.ic_artist)
            .circleCrop()
            .into(binding.imageArtistDetailAvatar)
    }
}