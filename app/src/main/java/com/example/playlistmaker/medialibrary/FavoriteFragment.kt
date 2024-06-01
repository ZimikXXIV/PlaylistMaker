package com.example.playlistmaker.medialibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentMedialibraryFavoriteBinding
import com.example.playlistmaker.medialibrary.viewmodel.FavoriteViewModel
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BindingFragment<FragmentMedialibraryFavoriteBinding>() {

    private val viewModel by viewModel<FavoriteViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMedialibraryFavoriteBinding {
        return FragmentMedialibraryFavoriteBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
