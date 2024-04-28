package com.example.playlistmaker.medialibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMedialibraryBinding
import com.example.playlistmaker.medialibrary.viewmodel.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryActivity : AppCompatActivity() {

    private val binding: ActivityMedialibraryBinding by lazy {
        ActivityMedialibraryBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModel<MediaLibraryViewModel>()

    private val tabMediator: TabLayoutMediator by lazy {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = favoriteTracks
                1 -> tab.text = playlists
                else -> {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPager.adapter = FragmentAdapter(supportFragmentManager, lifecycle)

        binding.btnBack.setOnClickListener() {
            finish()
        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    companion object {
        const val favoriteTracks = "Избранные треки"
        const val playlists = "Плейлисты"
    }
}
