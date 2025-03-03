package com.example.musicplayer.ui.home.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.databinding.FragmentAlbumHotBinding
import com.example.musicplayer.ui.home.HomeViewModel
import com.example.musicplayer.ui.home.album.detail.DetailAlbumFragment
import com.example.musicplayer.ui.home.album.detail.DetailAlbumViewModel
import com.example.musicplayer.ui.home.album.more.MoreAlbumFragment
import com.example.musicplayer.ui.home.album.more.MoreAlbumViewModel

class AlbumHotFragment : Fragment() {
    private lateinit var binding: FragmentAlbumHotBinding
    private lateinit var adapter: AlbumAdapter
    private val albumViewModel: AlbumHotViewModel by activityViewModels()
    private val detailAlbumViewModel: DetailAlbumViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels{
        val application = requireActivity().application as MusicApplication
        HomeViewModel.Factory(application.getSongRepository())
    }
    private val moreAlbumViewModel: MoreAlbumViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumHotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {

        adapter = AlbumAdapter(object : AlbumAdapter.OnAlbumClickListener {
            override fun onAlbumClick(album: Album) {
                detailAlbumViewModel.setAlbum(album)
                val songs = homeViewModel.songs.value
                detailAlbumViewModel.extractSongs(album, songs)
                navigateToDetailAlbum()
            }
        })
        binding.rvAlbumHot.adapter = adapter
        binding.textTitleAlbumHot.setOnClickListener {
            navigateToMorelAlbum()
        }
        binding.btnMoreAlbumHot.setOnClickListener {
            navigateToMorelAlbum()
        }
    }

    private fun navigateToMorelAlbum() {
        val albums = homeViewModel.albums.value
        albums?.let {
            moreAlbumViewModel.setAlbums(it)
        }
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, MoreAlbumFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    private fun observeData() {
        albumViewModel.albums.observe(viewLifecycleOwner) { albums ->
            adapter.updateAlbums(albums.sortedBy {
                -it.size
            }.subList(0, 10))

        }
    }

    private fun navigateToDetailAlbum() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, DetailAlbumFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }
}