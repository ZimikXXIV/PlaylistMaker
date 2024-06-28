package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.presentation.viewmodel.FavoriteViewModel
import com.example.playlistmaker.medialibrary.presentation.viewmodel.MediaLibraryViewModel
import com.example.playlistmaker.medialibrary.presentation.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.example.playlistmaker.playlist.presentation.EditPlaylistViewModel
import com.example.playlistmaker.playlist.presentation.NewPlaylistViewModel
import com.example.playlistmaker.playlist.presentation.ViewPlaylistViewModel
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
        PlayerViewModel(track, get(), get(), get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        MediaLibraryViewModel()
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        MainViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }

    viewModel {
        ViewPlaylistViewModel(get(), get())
    }

    viewModel {
        EditPlaylistViewModel(get(), get())
    }
}
