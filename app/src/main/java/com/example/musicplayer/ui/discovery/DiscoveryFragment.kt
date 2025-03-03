package com.example.musicplayer.ui.discovery

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentDiscoveryBinding
import com.example.musicplayer.ui.discovery.artist.ArtistViewModel
import com.example.musicplayer.ui.discovery.foryou.ForYouViewModel
import com.example.musicplayer.ui.discovery.mostheard.MostHeardViewModel

class DiscoveryFragment : Fragment() {
    private lateinit var binding: FragmentDiscoveryBinding
    private val artistViewModel: ArtistViewModel by activityViewModels()
    private val mostHeardViewModel: MostHeardViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        MostHeardViewModel.Factory(application.getSongRepository())
    }
    private val discoveryViewModel: DiscoveryViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        DiscoveryViewModel.Factory(application.getArtistRepository(), application.getSongRepository())
    }

    private val forYouViewModel: ForYouViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        ForYouViewModel.Factory(application.getSongRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        discoveryViewModel.top15Artists.observe(viewLifecycleOwner) { artists ->
            artistViewModel.setArtists(artists)
        }
        discoveryViewModel.artists.observe(viewLifecycleOwner) { artists ->
           // discoveryViewModel.saveArtistToDB()
            discoveryViewModel.saveArtistSongCrossRef(artists)
        }
        discoveryViewModel.top15MostHeardSongs.observe(viewLifecycleOwner) { songs ->
            mostHeardViewModel.setSongs(songs)
        }
        discoveryViewModel.top15ForYouSongs.observe(viewLifecycleOwner) { songs ->
            forYouViewModel.setSongs(songs)
        }
    }
}