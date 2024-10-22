package com.example.musicplayer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.ui.home.album.AlbumHotViewModel
import com.example.musicplayer.ui.home.recommended.RecommendedViewModel

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val homeView: HomeViewModel by activityViewModels()
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
        if(!isObserved) {
            setupObserver()
            isObserved = true
        }
    }

    private fun setupObserver() {
        homeView.albums.observe(viewLifecycleOwner) {
            albumViewModel.setAlbums(it)
        }
        homeView.songs.observe(viewLifecycleOwner) {
            songViewModel.setSongs(it)
        }
    }
}