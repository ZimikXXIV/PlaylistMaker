package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.search.data.impl.HistoryTrackRepositoryImpl
import com.example.playlistmaker.search.data.impl.SearchTrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchTrackRepository> {
        SearchTrackRepositoryImpl(get())
    }

    single<HistoryTrackRepository> {
        HistoryTrackRepositoryImpl(
            get(named(HistoryTrackRepositoryImpl.PLAYLISTMAKER_PREFERENCES)),
            get()
        )
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            androidContext(),
            get(named(SearchConst.SHAREDPREFERENCES_TAG)),
            get()
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}