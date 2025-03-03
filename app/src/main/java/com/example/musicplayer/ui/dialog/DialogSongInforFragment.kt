package com.example.musicplayer.ui.dialog

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentDialogSongInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogSongInforFragment : BottomSheetDialogFragment() {
    private val view :DialogSongInforViewModel by activityViewModels()
    private lateinit var binding : FragmentDialogSongInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogSongInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        view.song.observe(viewLifecycleOwner) { song ->
            showSongInfor(song)
        }
    }

    @SuppressLint("StringFormatMatches", "StringFormatInvalid")
    private fun showSongInfor(song: Song){
        val title = getString(R.string.text_song_detail_title, song.title)
        val artist = getString(R.string.text_song_detail_artist, song.artist)
        val album = getString(R.string.text_song_detail_album, song.album)
        val duration = getString(R.string.text_song_detail_duration, song.duration)
        val counter = getString(R.string.text_song_detail_counter, song.counter)
        val replay = getString(R.string.text_song_detail_replay_counter, song.replay)
        val favorite = getString(
            R.string.text_song_detail_favorite_status,
            if (song.favorite) getString(R.string.yes) else getString(R.string.no)
        )
        val notAvailable = getString(R.string.not_available)
        val genre = getString(R.string.text_song_detail_genre, notAvailable)
        val year = getString(R.string.text_song_detail_year, notAvailable)

        Glide.with(this)
            .load(song.image)
            .error(R.drawable.ic_album)
            .circleCrop()
            .into(binding.imageDetailSongArtwork)
        binding.textSongDetailTitle.text = title
        binding.textSongDetailArtist.text = artist
        binding.textSongDetailAlbum.text = album
        binding.textSongDetailDuration.text = duration
        binding.textSongDetailCounter.text = counter
        binding.textSongDetailReplayCounter.text = replay
        binding.textSongDetailFavoriteStatus.text = favorite
        binding.textSongDetailGenre.text = genre
        binding.textSongDetailYear.text = year
    }

    companion object {
        fun newInstance() = DialogSongInforFragment()
        const val TAG = "DialogSongInforFragment"
    }

}