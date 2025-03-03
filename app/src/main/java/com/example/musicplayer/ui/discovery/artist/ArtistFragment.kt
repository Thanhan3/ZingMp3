package com.example.musicplayer.ui.discovery.artist

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.artist.Artist
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.ui.discovery.artist.detail.ArtistDetailFragment
import com.example.musicplayer.ui.discovery.artist.detail.ArtistDetailViewModel
import com.example.musicplayer.ui.discovery.artist.more_artist.MoreArtistFragment

class ArtistFragment : Fragment() {
    private lateinit var binding: FragmentArtistBinding
    private val artistViewModel: ArtistViewModel by activityViewModels()
    private lateinit var adapter: ArtistAdapter
    private val artistDetailViewModel: ArtistDetailViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        ArtistDetailViewModel.Factory(application.getArtistRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        adapter = ArtistAdapter(
            object : ArtistAdapter.ArtistListener {
                override fun onClick(artist: Artist) {
                    artistDetailViewModel.setArtist(artist)
                    artistDetailViewModel.getArtistWithSongs(artist.id)
                    navigateToArtistDetail()
                }
            }
        )
        binding.includeArtistList.rvArtist.adapter = adapter
        binding.btnMoreArtist.setOnClickListener {
            navigateToMoreArtist()
        }
        binding.textTitleArtist.setOnClickListener {
            navigateToMoreArtist()
        }
    }

    private fun navigateToArtistDetail() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, ArtistDetailFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToMoreArtist() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, MoreArtistFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    private fun observeData() {
        artistViewModel.artists.observe(viewLifecycleOwner) { artists ->
            adapter.updateArtists(artists)
        }
    }
}