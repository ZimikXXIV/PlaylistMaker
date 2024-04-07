package com.example.playlistmaker.Creator

import android.content.Context
import com.example.playlistmaker.Player.data.PlayerRepositoryImpl
import com.example.playlistmaker.Player.domain.api.PlayerInteractor
import com.example.playlistmaker.Player.domain.api.PlayerRepository
import com.example.playlistmaker.Player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.Search.data.impl.SearchTrackRepositoryImpl
import com.example.playlistmaker.Search.data.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.Search.data.network.RetrofitClient
import com.example.playlistmaker.Search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.Search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.Search.domain.api.SearchTrackRepository
import com.example.playlistmaker.Search.domain.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.Search.domain.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.Settings.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.Settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.Settings.domain.api.ExternalNavigator
import com.example.playlistmaker.Settings.domain.api.SettingsRepository
import com.example.playlistmaker.Settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.Settings.domain.impl.SharingInteractorImpl

object Creator {
    fun getHistoryTrackInteractor(context: Context): HistoryTrackInteractorImpl {
        return HistoryTrackInteractorImpl(getHistoryTrackRepository(context))
    }
    private fun getHistoryTrackRepository(context: Context): HistoryTrackRepository {
        return TrackHistoryRepositoryImpl(context)
    }

    fun getSearchTrackInteractor(context: Context): SearchTrackInteractor {
        return SearchTrackInteractorImpl(getSearchTrackRepository(context))
    }

    private fun getSearchTrackRepository(context: Context): SearchTrackRepository {
        return SearchTrackRepositoryImpl(RetrofitClient(context))
    }

    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun getSettingsInteractor(context: Context): SettingsInteractorImpl {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun getSharingInteractor(context: Context): SharingInteractorImpl {
        return SharingInteractorImpl(context, getSharingRepository(context))
    }

    private fun getSharingRepository(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }
}