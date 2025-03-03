package com.example.musicplayer.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.u.SongOptionMenuDialogFragment
import com.example.musicplayer.ui.dialog.SongOptionMenuViewModel
import com.example.musicplayer.ui.viewmodel.SharedViewModel

open class PlayerBaseFragment : Fragment() {
    protected fun showOptionMenu(song: Song) {
        val menuDialogFragment = SongOptionMenuDialogFragment.newInstance
        val menuDialogViewModel: SongOptionMenuViewModel by activityViewModels()
        menuDialogViewModel.setSong(song)
        menuDialogFragment.show(
            requireActivity().supportFragmentManager,
            SongOptionMenuDialogFragment.TAG
        )
    }

    protected fun playSong(song: Song, index: Int, playlistName: String) {
        SharedViewModel.instance?.setCurrentPlaylist(playlistName)
        SharedViewModel.instance?.setIndexToPlay(index)
    }

}