package com.example.musicplayer.ui.home.album

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicplayer.R
import com.example.musicplayer.data.model.album.Album
import com.example.musicplayer.databinding.FragmentAlbumHotBinding

class AlbumHotFragment : Fragment() {
    private lateinit var binding: FragmentAlbumHotBinding
    private lateinit var adapter: AlbumAdapter
    private val albumViewModel: AlbumHotViewModel by viewModels()


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
        setupObserver()
    }

    private fun setupObserver() {
        albumViewModel.albums.observe(viewLifecycleOwner){albums ->
            adapter.updateAlbum(albums.sortedBy {
                it.size
            }.reversed().subList(0,10))

        }
    }

    private fun setupView() {
        adapter = AlbumAdapter(object : AlbumAdapter.OnAlbumClickListener{
            override fun onAlbumClick(album: Album) {
                //
            }
        })
        binding.rvAlbumHot.adapter=adapter
    }
}