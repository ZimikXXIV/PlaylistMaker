package com.example.playlistmaker.medialibrary.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.medialibrary.ui.Fragments.FavoriteFragment
import com.example.playlistmaker.medialibrary.ui.Fragments.PlaylistFragment

class AdapterFragment(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoriteFragment.newInstance() else PlaylistFragment.newInstance()
    }

}
