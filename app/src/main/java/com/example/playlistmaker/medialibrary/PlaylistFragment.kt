package com.example.playlistmaker.medialibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentMedialibraryPlaylistBinding
import com.example.playlistmaker.medialibrary.viewmodel.PlaylistViewModel
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BindingFragment<FragmentMedialibraryPlaylistBinding>() {


    private val viewModel by viewModel<PlaylistViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMedialibraryPlaylistBinding {
        return FragmentMedialibraryPlaylistBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}
