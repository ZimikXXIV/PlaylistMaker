package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.data.db.TrackDbConvertor
import com.example.playlistmaker.medialibrary.data.impl.FavoriteRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.api.FavoriteRepository
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.playlist.data.PlaylistDbConvertor
import com.example.playlistmaker.playlist.data.impl.FileSaveRepositoryImpl
import com.example.playlistmaker.playlist.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.playlist.domain.api.FileSaveRepository
import com.example.playlistmaker.playlist.domain.api.PlaylistRepository
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
        PlayerRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            androidContext(),
            get(named(SearchConst.SHAREDPREFERENCES_TAG))
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory { TrackDbConvertor() }
    factory { PlaylistDbConvertor() }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }
    single<FileSaveRepository> {
        FileSaveRepositoryImpl(androidContext())
    }

}