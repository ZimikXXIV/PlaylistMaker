package com.example.playlistmaker.medialibrary.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentMedialibraryFavoriteBinding
import com.example.playlistmaker.medialibrary.presentation.viewmodel.FavoriteViewModel
import com.example.playlistmaker.medialibrary.ui.State.FavoriteState
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.api.TrackListClickListenerInterface
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.BindingFragment
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BindingFragment<FragmentMedialibraryFavoriteBinding>(),
    TrackListClickListenerInterface {

    private val viewModel by viewModel<FavoriteViewModel>()
    private val favoriteTrackAdapter = TrackAdapter(this)
    private lateinit var trackClickDebounce: (Track) -> Unit
    private val favoriteList = ArrayList<Track>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMedialibraryFavoriteBinding {
        return FragmentMedialibraryFavoriteBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackClickDebounce = debounce<Track>(
            SearchConst.CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            openPlayer(track)
        }

        favoriteTrackAdapter.setTrackList(favoriteList)
        binding.recyclerFavoriteList.layoutManager =
            LinearLayoutManager(binding.recyclerFavoriteList.context)
        binding.recyclerFavoriteList.adapter = favoriteTrackAdapter

        viewModel.getFavoriteLiveData().observe(viewLifecycleOwner) { favoriteState ->
            updateView(favoriteState)
        }
    }

    fun updateView(favoriteState: FavoriteState) {

        binding.recyclerFavoriteList.isVisible = false
        binding.layoutEmptyFavoriteList.isVisible = false
        binding.layoutFavoriteProgressBar.isVisible = false

        when (favoriteState) {
            is FavoriteState.FavoriteList -> {
                favoriteTrackAdapter.setTrackList(favoriteState.favoriteTracks)
                binding.recyclerFavoriteList.isVisible = true
            }

            is FavoriteState.EmptyList -> {
                favoriteTrackAdapter.setTrackList(emptyList())
                binding.layoutEmptyFavoriteList.isVisible = true
            }

            is FavoriteState.ProgressBar -> {
                favoriteTrackAdapter.setTrackList(emptyList())
                binding.layoutFavoriteProgressBar.isVisible = true
            }

            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    fun openPlayer(track: Track) {
        val intent = Intent(activity, AudioPlayerActivity::class.java)
        intent.putExtra(PlayerConst.TRACK_INFO, track)
        requireActivity().startActivity(intent)
    }

    override fun onClick(track: Track) {
        trackClickDebounce(track)
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
