package com.example.musicplayer.ui.home.recommended

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentRecommendedBinding

class RecommendedFragment : Fragment() {
    private lateinit var binding: FragmentRecommendedBinding
    private lateinit var adapter: SongAdapter

    private val viewModel: RecommendedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendedBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs)
        }
    }

    private fun setupView() {
        adapter = SongAdapter(
            object : SongAdapter.OnSongClickListener{
                override fun onSongClick(song: Song, position : Int) {

                }
            },
            object : SongAdapter.OnSongOptionMenuClickListener{
                override fun onSongOptionMenuClick(position: Int) {

                }
            }
        )
        binding.includeSongList.rvSongList.adapter = adapter


    }
}