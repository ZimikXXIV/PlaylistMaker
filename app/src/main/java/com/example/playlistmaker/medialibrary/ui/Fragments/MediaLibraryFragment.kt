package com.example.playlistmaker.medialibrary.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMedialibraryBinding
import com.example.playlistmaker.medialibrary.presentation.viewmodel.MediaLibraryViewModel
import com.example.playlistmaker.medialibrary.ui.adapters.AdapterFragment
import com.example.playlistmaker.utils.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : BindingFragment<FragmentMedialibraryBinding>() {

    private val viewModel by viewModel<MediaLibraryViewModel>()

    private lateinit var tabMediator: TabLayoutMediator

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMedialibraryBinding {
        return FragmentMedialibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = AdapterFragment(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_track)
                1 -> tab.text = getString(R.string.playlists)
                else -> {}
            }
        }

        tabMediator.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()

    }

}
