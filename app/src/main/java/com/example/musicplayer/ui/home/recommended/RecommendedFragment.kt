package com.example.musicplayer.ui.home.recommended

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentRecommendedBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.home.HomeViewModel
import com.example.musicplayer.ui.home.recommended.more.MoreRecommendedFragment
import com.example.musicplayer.ui.home.recommended.more.MoreRecommendedViewModel

class RecommendedFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentRecommendedBinding
    private lateinit var adapter: SongAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val moreRecommendedViewModel: MoreRecommendedViewModel by activityViewModels()
    private val viewModel: RecommendedViewModel by activityViewModels()


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
            adapter.updateSongs(songs.subList(0, 18))
        }
    }

    private fun setupView() {
        adapter = SongAdapter(
            object : SongAdapter.OnSongClickListener {
                override fun onSongClick(song: Song, position: Int) {

                }
            },
            object : SongAdapter.OnSongOptionMenuClickListener {
                override fun onSongOptionMenuClick(position: Int) {
                    showOptionMenu(viewModel.songs.value!![position])
                }
            }
        )
        binding.includeSongList.rvSongList.adapter = adapter
        binding.textTitleRecommended.setOnClickListener {
            navigateToMoreSong()
        }
        binding.btnMoreRecommended.setOnClickListener {
            navigateToMoreSong()
        }
    }

    private fun navigateToMoreSong() {
        val songs = homeViewModel.songs.value
        songs?.let { moreRecommendedViewModel.setSong(songs) }
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, MoreRecommendedFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }
}