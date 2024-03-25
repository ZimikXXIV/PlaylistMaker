package com.example.playlistmaker.Creator

import android.content.Context
import com.example.playlistmaker.Player.data.PlayerInteractorImpl
import com.example.playlistmaker.Player.domain.api.PlayerInteractor
import com.example.playlistmaker.Search.data.SearchTrackRepositoryImpl
import com.example.playlistmaker.Search.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.Search.data.network.RetrofitClient
import com.example.playlistmaker.Search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.Search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.Search.domain.api.SearchTrackRepository
import com.example.playlistmaker.Search.domain.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.Search.domain.impl.SearchTrackInteractorImpl

object Creator {
    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    fun getHistoryTrackInteractor(context: Context): HistoryTrackInteractorImpl {
        return HistoryTrackInteractorImpl(getHistoryTrackRepository(context))
    }

    private fun getHistoryTrackRepository(context: Context): HistoryTrackRepository {
        return TrackHistoryRepositoryImpl(context)
    }

    private fun getSearchTrackRepository(): SearchTrackRepository {
        return SearchTrackRepositoryImpl(RetrofitClient())
    }

    fun provideSearchTrackInteractor(): SearchTrackInteractor {
        return SearchTrackInteractorImpl(getSearchTrackRepository())
    }

}