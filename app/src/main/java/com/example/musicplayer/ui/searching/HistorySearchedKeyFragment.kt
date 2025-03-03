package com.example.musicplayer.ui.searching

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.search.HistorySearchedKey
import com.example.musicplayer.databinding.FragmentHistorySearchedKeyBinding
import com.example.musicplayer.databinding.ItemSearchedKeyBinding
import com.example.musicplayer.ui.dialog.ConfirmationDialogFragment


class HistorySearchedKeyFragment : Fragment() {
    private lateinit var binding: FragmentHistorySearchedKeyBinding
    private lateinit var adapter: HistorySearchedKeyAdapter

    private val viewModel: SearchingViewModel by activityViewModels  {
        val application = requireActivity().application as MusicApplication
        SearchingViewModel.Factory(application.getSearchRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistorySearchedKeyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        binding.tvClearHistoryKeys.setOnClickListener {
            val message = R.string.message_confirm_clear_history
            val dialog = ConfirmationDialogFragment(message,
                object : ConfirmationDialogFragment.OnDeleteConfirmListener {
                    override fun onConfirm(isConfirmed: Boolean) {
                        if (isConfirmed) {
                            viewModel.clearHistoryKeys()
                        }
                    }
                })
            dialog.show(requireActivity().supportFragmentManager, ConfirmationDialogFragment.TAG)
        }
        adapter = HistorySearchedKeyAdapter(object : OnItemClickListener {
            override fun onClick(key: String) {
                viewModel.setSelectedKey(key)
            }
        })
        binding.rvHistorySearchedKey.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.keys.observe(viewLifecycleOwner) { keys ->
            adapter.updateKeys(keys)
        }
    }

    internal class HistorySearchedKeyAdapter(
        private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<HistorySearchedKeyAdapter.ViewHolder>() {
        private val keys = mutableListOf<HistorySearchedKey>()

        internal class ViewHolder(
            private val binding: ItemSearchedKeyBinding,
            private val listener: OnItemClickListener
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(key: HistorySearchedKey) {
                binding.tvSearchedKey.text = key.key
                binding.root.setOnClickListener {
                    listener.onClick(key.key)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSearchedKeyBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding, listener)
        }

        override fun getItemCount(): Int {
            return keys.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(keys[position])
        }

        @SuppressLint("NotifyDataSetChanged")
        fun updateKeys(newKeys: List<HistorySearchedKey>) {
            keys.clear()
            keys.addAll(newKeys)
            notifyDataSetChanged()
        }
    }

    internal interface OnItemClickListener {
        fun onClick(key: String)
    }
}