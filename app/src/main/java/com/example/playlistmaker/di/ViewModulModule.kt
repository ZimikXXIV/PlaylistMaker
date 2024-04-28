package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.viewmodel.FavoriteViewModel
import com.example.playlistmaker.medialibrary.viewmodel.MediaLibraryViewModel
import com.example.playlistmaker.medialibrary.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.example.playlistmaker.root.presentation.MainViewModel
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.example.playlistmaker.settings.presentation.viewmodel.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel { (track: TrackInfo) ->
        PlayerViewModel(track, get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }

    viewModel {
        FavoriteViewModel()
    }

    viewModel {
        MediaLibraryViewModel()
    }

    viewModel {
        PlaylistViewModel()
    }

    viewModel {
        MainViewModel(get())
    }

}
