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
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import com.example.musicplayer.utils.MusicAppUtils


class MoreRecommendedFragment : PlayerBaseFragment() {
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
        val playlistName = MusicAppUtils.DefaultPlaylistName.RECOMMENDED.value
        moreRecommendedViewModel.songs.observe(viewLifecycleOwner){ songs ->
            adapter.updateSongs(songs)
            SharedViewModel.instance?.setupPlaylist(songs, playlistName)
        }
    }

    private fun setupView() {
        binding.toolbarMoreRecommended.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        adapter = SongAdapter(
            object : SongAdapter.OnSongClickListener{
                override fun onSongClick(song: Song, position : Int) {
                    val playlistName = MusicAppUtils.DefaultPlaylistName.RECOMMENDED.value
                    playSong(song, position, playlistName)
                }
            },
            object : SongAdapter.OnSongOptionMenuClickListener{
                override fun onSongOptionMenuClick(position: Int) {
                    showOptionMenu(moreRecommendedViewModel.songs.value!![position])
                }
            }
        )
        binding.includeMoreRecommended.rvSongList.adapter =adapter

    }
}