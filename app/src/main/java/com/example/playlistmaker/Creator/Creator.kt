package com.example.playlistmaker.Creator

import android.content.Context
import com.example.playlistmaker.Player.data.PlayerRepositoryImpl
import com.example.playlistmaker.Player.domain.api.PlayerInteractor
import com.example.playlistmaker.Player.domain.api.PlayerRepository
import com.example.playlistmaker.Player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.Search.data.SearchTrackRepositoryImpl
import com.example.playlistmaker.Search.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.Search.data.network.RetrofitClient
import com.example.playlistmaker.Search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.Search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.Search.domain.api.SearchTrackRepository
import com.example.playlistmaker.Search.domain.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.Search.domain.impl.SearchTrackInteractorImpl

object Creator {
    fun getHistoryTrackInteractor(context: Context): HistoryTrackInteractorImpl {
        return HistoryTrackInteractorImpl(getHistoryTrackRepository(context))
    }
    private fun getHistoryTrackRepository(context: Context): HistoryTrackRepository {
        return TrackHistoryRepositoryImpl(context)
    }

    fun provideSearchTrackInteractor(): SearchTrackInteractor {
        return SearchTrackInteractorImpl(getSearchTrackRepository())
    }
    private fun getSearchTrackRepository(): SearchTrackRepository {
        return SearchTrackRepositoryImpl(RetrofitClient())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }


}