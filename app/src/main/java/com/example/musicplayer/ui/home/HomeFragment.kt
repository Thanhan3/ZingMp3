package com.example.musicplayer.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R

import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.ui.home.album.AlbumHotViewModel
import com.example.musicplayer.ui.home.recommended.RecommendedViewModel
import com.example.musicplayer.ui.searching.SearchingFragment

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        HomeViewModel.Factory(application.getSongRepository())
    }
    private val albumViewModel: AlbumHotViewModel by activityViewModels()
    private val songViewModel: RecommendedViewModel by activityViewModels()
    private var isObserved = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isObserved) {
            setupObserver()
            isObserved = true
        }
        setupBtnSearch()
    }

    @SuppressLint("DetachAndAttachSameFragment")
    private fun setupBtnSearch() {
        _binding.btnSearchHome.setOnClickListener {
            val searchFragment = SearchingFragment()
            _binding.btnSearchHome.setOnClickListener {
                findNavController().navigate(R.id.searchFragment)
            }
        }
    }

    private fun setupObserver() {
        homeViewModel.albums.observe(viewLifecycleOwner) {
            albumViewModel.setAlbums(it)
        }
        homeViewModel.localSongs.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                homeViewModel.songs.observe(viewLifecycleOwner) { remoteSongs ->
                    songViewModel.setSongs(remoteSongs)
                    homeViewModel.saveSongsToDB()
                }
            } else {
                songViewModel.setSongs(it)
            }
        }
    }
}