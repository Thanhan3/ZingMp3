package com.example.musicplayer.ui.home.recommended.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentMoreRecommendedBinding
import com.example.musicplayer.ui.home.recommended.SongAdapter


class MoreRecommendedFragment : Fragment() {
    private lateinit var binding: FragmentMoreRecommendedBinding
    private val moreRecommendedViewModel: MoreRecommendedViewModel by activityViewModels()
    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreRecommendedBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        moreRecommendedViewModel.songs.observe(viewLifecycleOwner){ songs ->
            adapter.updateSongs(songs)
        }
    }

    private fun setupView() {
        binding.toolbarMoreRecommended.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
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
        binding.includeMoreRecommended.rvSongList.adapter =adapter

    }
}