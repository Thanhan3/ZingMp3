package com.example.musicplayer.ui.searching

import android.app.SearchManager
import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentSearchingBinding
import com.example.musicplayer.ui.PlayerBaseFragment
import com.example.musicplayer.ui.home.recommended.SongAdapter
import com.example.musicplayer.utils.MusicAppUtils

class SearchingFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentSearchingBinding
    private lateinit var songAdapter: SongAdapter

    private val viewModel: SearchingViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        SearchingViewModel.Factory(application.getSearchRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupSearchView()
        setupObserver()
    }

    private fun setupView() {
        binding.toolbarSearching.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        songAdapter = SongAdapter(
            object : SongAdapter.OnSongClickListener {
                override fun onSongClick(song: Song, position: Int) {
                    viewModel.insertSelectedSong(song)
                    play(song)
                }

            },
            object : SongAdapter.OnSongOptionMenuClickListener {
                override fun onSongOptionMenuClick(position: Int) {
                    showOptionMenu(viewModel.songs.value!![position])
                }

            }
        )
        binding.includeSearchedResult.rvSearchedSong.adapter = songAdapter
    }

    private fun play(song: Song) {
        viewModel.updatePlaylist(song)
        val playlistName = MusicAppUtils.DefaultPlaylistName.SEARCH.value
        playSong(song, 0, playlistName)
    }

    private fun setupSearchView() {
        val manager: SearchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchViewHome.setSearchableInfo(
            manager.getSearchableInfo(requireActivity().componentName)
        )
        binding.searchViewHome.isIconifiedByDefault = false
        binding.searchViewHome.isSubmitButtonEnabled = true
        binding.searchViewHome.isQueryRefinementEnabled = true
        binding.searchViewHome.onActionViewExpanded()
        binding.searchViewHome.maxWidth = Int.MAX_VALUE
        binding.searchViewHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                activeSearchResultLayout(true)
                if (query.trim().isNotEmpty()) {
                    performSearch(query)
                    viewModel.insertSearchedKey(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.trim().isNotEmpty()) {
                    activeSearchResultLayout(true)
                    performSearch(newText)
                } else {
                    activeSearchResultLayout(false)
                }
                return true
            }
        })
    }

    private fun activeSearchResultLayout(shouldShowSearchResult: Boolean) {
        val searchResultVisibility = if (shouldShowSearchResult) View.VISIBLE else View.GONE
        val historyVisibility = if (!shouldShowSearchResult) View.VISIBLE else View.GONE
        binding.includeSearchedHistory.fcvHistorySearchedKey.visibility = historyVisibility
        binding.includeSearchedHistory.fcvRecentSearchedSong.visibility = historyVisibility
        binding.includeSearchedResult.rvSearchedSong.visibility = searchResultVisibility
    }

    private fun setupObserver() {
        viewModel.songs.observe(viewLifecycleOwner) { songs ->
            songAdapter.updateSongs(songs)
        }
        viewModel.searchedKey.observe(viewLifecycleOwner) { key ->
            binding.searchViewHome.setQuery(key, false)
        }
    }

    private fun performSearch(key: String) {
        viewModel.search(key)
    }
}