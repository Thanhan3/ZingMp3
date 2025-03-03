package com.example.musicplayer.u

import com.example.musicplayer.ui.dialog.DialogSongInforFragment
import com.example.musicplayer.ui.dialog.DialogSongInforViewModel
import com.example.musicplayer.ui.dialog.MenuItem
import com.example.musicplayer.ui.dialog.SongOptionMenuViewModel



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.playlist.Playlist
import com.example.musicplayer.data.model.song.Song
import com.example.musicplayer.databinding.DialogFragmentSongOptionMenuBinding
import com.example.musicplayer.databinding.ItemOptionMenuBinding
import com.example.musicplayer.ui.dialog.DialogAddSongToPlaylistFragment
import com.example.musicplayer.ui.library.playlist.PlaylistViewModel
import com.example.musicplayer.utils.OptionMenuUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SongOptionMenuDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: DialogFragmentSongOptionMenuBinding
    private lateinit var adapter: MenuItemAdapter
    private val viewModel: SongOptionMenuViewModel by activityViewModels()
    private val songInfoViewModel: DialogSongInforViewModel by activityViewModels()

    private val playlistViewModel: PlaylistViewModel by activityViewModels {
        val application = requireActivity().application as MusicApplication
        PlaylistViewModel.Factory(application.getPlaylistRepository())
    }
    private var isClicked = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentSongOptionMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.optionMenuItems.observe(viewLifecycleOwner) { menuItems ->
            adapter.updateMenuItems(menuItems)
        }
        viewModel.song.observe(viewLifecycleOwner) { song ->
            showSongInfo(song)
        }
        playlistViewModel.addResult.observe(viewLifecycleOwner) { addResult ->
            if (isClicked) {
                val messageId = if (addResult) {
                    R.string.add_to_playlist_success
                } else {
                    R.string.add_to_playlist_failed
                }
                val playlistName = viewModel.playlistName.value ?: ""
                val message = requireActivity().getString(messageId, playlistName)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                isClicked = false
            }
        }
    }

    private fun showSongInfo(song: Song) {
        binding.includeSongBottomSheet.textOptionItemSongTitle.text = song.title
        binding.includeSongBottomSheet.textOptionItemSongArtist.text = song.artist
        Glide.with(requireContext())
            .load(song.image)
            .error(R.drawable.ic_album)
            .into(binding.includeSongBottomSheet.imageOptionSongArtwork)
    }

    private fun setupView() {
        adapter = MenuItemAdapter(
            listener = object : MenuItemAdapter.OnOptionMenuItemClickListener {
                override fun onItemClick(menuItem: MenuItem) {
                    onMenuClick(menuItem)
                }
            }
        )
        binding.rvOptionMenu.adapter = adapter
    }

    private fun onMenuClick(menuItem: MenuItem) {
        when (menuItem.option) {
            OptionMenuUtils.OptionMenu.DOWNLOAD -> downloadSong()
            OptionMenuUtils.OptionMenu.VIEW_SONG_INFORMATION -> showDetailSongInfo()
            OptionMenuUtils.OptionMenu.ADD_TO_FAVOURITES -> addToFavorite()
            OptionMenuUtils.OptionMenu.ADD_TO_PLAYLIST -> addToPlaylist()
            OptionMenuUtils.OptionMenu.ADD_TO_QUEUE -> addToQueue()
            OptionMenuUtils.OptionMenu.VIEW_ARTIST -> viewArtist()
            OptionMenuUtils.OptionMenu.VIEW_ALBUM -> viewAbum()
            OptionMenuUtils.OptionMenu.BLOCK -> block()
            OptionMenuUtils.OptionMenu.REPORT_ERROR -> report()
        }
    }

    private fun downloadSong() {
        // todo
    }

    private fun showDetailSongInfo() {
        songInfoViewModel.setSong(viewModel.song.value!!)
        DialogSongInforFragment.newInstance().show(
            requireActivity().supportFragmentManager,
            DialogSongInforFragment.TAG
        )
    }

    private fun addToFavorite() {
        viewModel.song.value?.let {
            it.favorite= true
        }
    }

    private fun addToPlaylist() {
        val tag = DialogAddSongToPlaylistFragment.TAG
        val dialog = DialogAddSongToPlaylistFragment(
            object : DialogAddSongToPlaylistFragment.OnPlaylistSelectedListener {
                override fun onPlaylistSelected(playlist: Playlist) {
                    val song = viewModel.song.value
                    viewModel.setPlaylistName(playlist.name)
                    playlistViewModel.createPlaylistSongCrossRef(playlist, song)
                    isClicked = true
                }
            }
        )
        dialog.show(requireActivity().supportFragmentManager, tag)
    }

    private fun addToQueue() {
        // todo
    }

    private fun viewArtist() {
        // todo
    }

    private fun viewAbum() {
        // todo
    }

    private fun block() {
        // todo
    }

    private fun report() {
        // todo
    }
    // Adapter for menu option list
    class MenuItemAdapter(
        private val menuItems : MutableList<MenuItem> = mutableListOf(),
        private val listener: OnOptionMenuItemClickListener
    ) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

        class ViewHolder(
            private val binding: ItemOptionMenuBinding,
            private val listener: OnOptionMenuItemClickListener
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(menuItem: MenuItem) {
                val titele = binding.root.context.getString(menuItem.menuItemTitle)
                binding.textItemMenuTitle.text = titele
                Glide.with(binding.root).load(menuItem.iconId).into(binding.imageItemMenuIcon)
                binding.root.setOnClickListener {
                    listener.onItemClick(menuItem)
                }
            }
        }

        interface OnOptionMenuItemClickListener {
            fun onItemClick(menuItem: MenuItem)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOptionMenuBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding, listener)
        }

        override fun getItemCount(): Int {
            return menuItems.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(menuItems[position])
        }
        fun updateMenuItems(items: List<MenuItem>) {
            menuItems.addAll(items)
            notifyItemRangeChanged(0,items.size)
        }

    }

    companion object{
        val newInstance = SongOptionMenuDialogFragment()
        val TAG ="SongOptionMenuDialogFragment"
    }
}
