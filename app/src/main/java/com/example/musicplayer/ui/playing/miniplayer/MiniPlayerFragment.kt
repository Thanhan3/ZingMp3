package com.example.musicplayer.ui.playing.miniplayer

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.activityViewModels
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.FragmentMiniPlayerBinding
import com.example.musicplayer.ui.playing.nowplaying.NowPlayingActivity
import com.example.musicplayer.ui.viewmodel.MediaPlayerViewModel
import com.example.musicplayer.ui.viewmodel.SharedViewModel
import com.example.musicplayer.utils.MusicAppUtils


class MiniPlayerFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentMiniPlayerBinding
    private val viewModel: MiniPlayerViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        val repository = application.getSongRepository()
        MiniPlayerViewModel.Factory(repository)
    }
    private var mediaController: MediaController? = null
    private lateinit var pressedAnimator: Animator
    private lateinit var rotationAnimator: ObjectAnimator
    private var currentFraction: Float = 0f
    private val nowPlayingActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            currentFraction = result.data
                ?.getFloatExtra(MusicAppUtils.EXTRA_CURRENT_FRACTION, 0f) ?: 0f
            mediaController?.let {
                if (it.isPlaying) {
                    rotationAnimator.start()
                    rotationAnimator.setCurrentFraction(currentFraction)
                    currentFraction = 0f
                } else {
                    rotationAnimator.setCurrentFraction(currentFraction)
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupAnimator()
        setupMediaController()
        setupObserve()
    }

    override fun onResume() {
        super.onResume()
        val currentPlayingSong = SharedViewModel.instance!!.playingSong.value?.song
        currentPlayingSong?.let {
            val songId = it.id
            viewModel.getCurrentPlayingSong(songId)
            viewModel.currentPlayingSong.observe(viewLifecycleOwner) { song ->
                if (song != null) {
                    updateFavoriteStatus(song)
                }
            }
        }
    }

    private fun setupView() {
        binding.btnMiniPlayerFavorite.setOnClickListener(this)
        binding.btnMiniPlayerPlayPause.setOnClickListener(this)
        binding.btnMiniPlayerSkipNext.setOnClickListener(this)
        binding.root.setOnClickListener {
            navigateToNowPlaying()
        }
    }

    private fun navigateToNowPlaying() {
        val intent = Intent(requireContext(), NowPlayingActivity::class.java).apply {
            rotationAnimator.pause()
            putExtra(MusicAppUtils.EXTRA_CURRENT_FRACTION, rotationAnimator.animatedFraction)
        }
//        val options = ActivityOptionsCompat
//            .makeCustomAnimation(requireContext(), R.anim.slide_up, R.anim.fade_out)
        nowPlayingActivityLauncher.launch(intent)
    }

    private fun setupMediaController() {
        MediaPlayerViewModel.instance.mediaController.observe(viewLifecycleOwner) { controller ->
            controller?.let {
                mediaController = it
                setupListener()
                setupObserveForMediaController()
            }
        }
    }

    private fun setupObserveForMediaController() {
        mediaController?.let {
            viewModel.mediaItems.observe(viewLifecycleOwner) { mediaItems ->
                it.setMediaItems(mediaItems)
            }
            SharedViewModel.instance?.indexToPlay?.observe(viewLifecycleOwner) { index ->
                if (index > -1 && it.mediaItemCount > index) {
                    it.seekTo(index, 0)
                    it.prepare()
//                    it.play()
                }
            }
        }
    }

    private fun setupListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                viewModel.setPlayingState(isPlaying)
            }
        })
    }

    private fun setupObserve() {
        SharedViewModel.instance?.playingSong?.observe(viewLifecycleOwner) {
            it.song?.let { song ->
                showSongInfo(song)
                if (song.favorite) {
                    binding.btnMiniPlayerFavorite.setImageResource(R.drawable.ic_favorite_on)
                } else {
                    binding.btnMiniPlayerFavorite.setImageResource(R.drawable.ic_favorite)
                }
            }
        }
        SharedViewModel.instance?.currentPlaylist?.observe(viewLifecycleOwner) {
            viewModel.setMediaItem(it.mediaItems)
        }
        SharedViewModel.instance?.indexToPlay?.observe(viewLifecycleOwner) { index ->
            if (index > -1 && mediaController != null && mediaController!!.mediaItemCount > index) {
                mediaController!!.seekTo(index, 0)
                mediaController!!.prepare()
                mediaController!!.play()
            }
        }
        viewModel.mediaItems.observe(viewLifecycleOwner) { mediaItems ->
            mediaController?.setMediaItems(mediaItems)
        }
        viewModel.isPlaying.observe(viewLifecycleOwner) {
            if (it) { // playing
                binding.btnMiniPlayerPlayPause.setImageResource(R.drawable.ic_pause_circle)
                if (rotationAnimator.isPaused) {
                    if (currentFraction != 0f) {
                        rotationAnimator.start()
                        rotationAnimator.setCurrentFraction(currentFraction)
                        currentFraction = 0f
                    } else {
                        rotationAnimator.resume()
                    }
                } else if (!rotationAnimator.isRunning) {
                    rotationAnimator.start()
                }
            }else{
                binding.btnMiniPlayerPlayPause.setImageResource(R.drawable.ic_play_circle)
                rotationAnimator.pause()
            }
        }
    }

    private fun showSongInfo(song: Song) {
        binding.textMiniPlayerTitle.text = song.title
        binding.textMiniPlayerArtist.text = song.artist
        Glide.with(requireContext())
            .load(song.image)
            .error(R.drawable.ic_album)
            .circleCrop()
            .into(binding.imageMiniPlayerArtwork)
    }

    private fun setupAnimator() {
        pressedAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.button_pressed)
        rotationAnimator = ObjectAnimator
            .ofFloat(binding.imageMiniPlayerArtwork, "rotation", 0f, 360f)
        rotationAnimator.interpolator = LinearInterpolator()
        rotationAnimator.duration = 12000
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE
        rotationAnimator.repeatMode = ObjectAnimator.RESTART
    }


    private fun setupFavorite() {
        val playingSong = SharedViewModel.instance?.playingSong?.value
        playingSong?.let {
            val song = it.song
            song!!.favorite = !song.favorite
            updateFavoriteStatus(song)
            SharedViewModel.instance?.updateFavoriteStatus(song)
        }
    }

    private fun updateFavoriteStatus(song: Song) {
        SharedViewModel.instance?.playingSong?.observe(viewLifecycleOwner) {
            if (it.song!!.favorite) {
                binding.btnMiniPlayerFavorite.setImageResource(R.drawable.ic_favorite_on)
            } else {
                binding.btnMiniPlayerFavorite.setImageResource(R.drawable.ic_favorite)
            }
        }

    }

    override fun onClick(v: View?) {
        pressedAnimator.setTarget(v)
        pressedAnimator.start()
        when (v) {
            binding.btnMiniPlayerPlayPause -> {
                mediaController?.let {
                    if (it.isPlaying) {
                        it.pause()
                    } else {
                        it.play()
                    }
                }
            }

            binding.btnMiniPlayerSkipNext -> {
                mediaController?.let {
                    if (it.hasNextMediaItem()) {
                        it.seekToNextMediaItem()
                        it.play()
                        rotationAnimator.end()
                    }
                }
            }

            binding.btnMiniPlayerFavorite -> {
                setupFavorite()
            }
        }
    }


}