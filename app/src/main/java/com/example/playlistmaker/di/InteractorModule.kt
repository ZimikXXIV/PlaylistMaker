package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.domain.api.FavoriteInteractor
import com.example.playlistmaker.medialibrary.domain.impl.FavoriteInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.example.playlistmaker.search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.search.domain.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SearchTrackInteractor> {
        SearchTrackInteractorImpl(get())
    }

    single<HistoryTrackInteractor> {
        HistoryTrackInteractorImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

}