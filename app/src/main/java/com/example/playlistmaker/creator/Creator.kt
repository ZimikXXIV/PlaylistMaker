package com.example.playlistmaker.creator
/* Не актуален после dependency injection, сохранен для истории
import android.content.Context
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.impl.SearchTrackRepositoryImpl
import com.example.playlistmaker.search.data.impl.HistoryTrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.example.playlistmaker.search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    fun getHistoryTrackInteractor(context: Context): HistoryTrackInteractor {
        return HistoryTrackInteractorImpl(getHistoryTrackRepository(context))
    }
    private fun getHistoryTrackRepository(context: Context): HistoryTrackRepository {
        return HistoryTrackRepositoryImpl(context)
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
        return SharingInteractorImpl(getSharingRepository(context))
    }

    private fun getSharingRepository(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }
}

 */