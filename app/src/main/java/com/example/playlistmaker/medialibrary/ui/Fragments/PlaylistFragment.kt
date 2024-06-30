package com.example.playlistmaker.medialibrary.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMedialibraryPlaylistBinding
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.medialibrary.domain.api.PlaylistViewClickListenerInterface
import com.example.playlistmaker.medialibrary.presentation.viewmodel.PlaylistViewModel
import com.example.playlistmaker.medialibrary.ui.State.PlaylistCardState
import com.example.playlistmaker.medialibrary.ui.adapters.PlaylistAdapter
import com.example.playlistmaker.playlist.ui.Fragments.ViewPlaylistFragment
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.utils.BindingFragment
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BindingFragment<FragmentMedialibraryPlaylistBinding>(),
    PlaylistViewClickListenerInterface {


    private val viewModel by viewModel<PlaylistViewModel>()

    private val playlistCardAdapter = PlaylistAdapter(this)
    private val playlistCards = ArrayList<PlaylistCard>()
    private lateinit var playlistClickDebounce: (PlaylistCard) -> Unit

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMedialibraryPlaylistBinding {
        return FragmentMedialibraryPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistClickDebounce = debounce<PlaylistCard>(
            SearchConst.CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { playlist ->
            openPlaylistView(playlist)
        }

        binding.btnCreateNewPlaylist.setOnClickListener() {
            findNavController().navigate(R.id.action_fragmentMediaLibrary_to_newPlaylistFragment)
        }

        playlistCardAdapter.setPlaylistCards(playlistCards)
        binding.recyclerViewPlaylists.layoutManager =
            GridLayoutManager(binding.recyclerViewPlaylists.context, 2)
        binding.recyclerViewPlaylists.adapter = playlistCardAdapter

        viewModel.getplaylistCardLiveData().observe(viewLifecycleOwner) { playlistCardState ->
            updateView(playlistCardState)
        }

    }

    override fun onClick(playlist: PlaylistCard) {
        playlistClickDebounce(playlist)
    }

    private fun openPlaylistView(playlist: PlaylistCard) {
        val args = Bundle()
        args.putSerializable(ViewPlaylistFragment.PLAYLIST_ID, playlist.id)
        findNavController().navigate(R.id.action_fragmentMediaLibrary_to_viewPlaylistFragment, args)
    }

    fun updateView(playlistCardState: PlaylistCardState) {

        binding.recyclerViewPlaylists.isVisible = false
        binding.layoutPlaylistProgressBar.isVisible = false
        binding.layoutEmptyPlaylistCards.isVisible = false
        binding.btnCreateNewPlaylist.isVisible = false

        when (playlistCardState) {
            is PlaylistCardState.PlaylistCards -> {
                playlistCardAdapter.setPlaylistCards(playlistCardState.playlistCards)
                binding.recyclerViewPlaylists.isVisible = true
                binding.btnCreateNewPlaylist.isVisible = true
            }

            is PlaylistCardState.EmptyCards -> {
                playlistCardAdapter.setPlaylistCards(emptyList())
                binding.layoutEmptyPlaylistCards.isVisible = true
                binding.btnCreateNewPlaylist.isVisible = true
            }

            is PlaylistCardState.ProgressBar -> {
                playlistCardAdapter.setPlaylistCards(emptyList())
                binding.layoutPlaylistProgressBar.isVisible = true
            }

            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}
