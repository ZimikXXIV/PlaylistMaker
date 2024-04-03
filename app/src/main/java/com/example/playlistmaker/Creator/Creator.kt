package com.example.playlistmaker.Creator

import android.app.Activity
import android.content.Context
import com.example.playlistmaker.Player.data.PlayerRepositoryImpl
import com.example.playlistmaker.Player.domain.api.PlayerInteractor
import com.example.playlistmaker.Player.domain.api.PlayerRepository
import com.example.playlistmaker.Player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.Player.presentation.InfoController
import com.example.playlistmaker.Search.data.SearchTrackRepositoryImpl
import com.example.playlistmaker.Search.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.Search.data.network.RetrofitClient
import com.example.playlistmaker.Search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.Search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.Search.domain.api.SearchTrackRepository
import com.example.playlistmaker.Search.domain.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.Search.domain.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.Search.presentation.TrackAdapter
import com.example.playlistmaker.Search.presentation.TrackSerachController

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

    fun getTrackSearchController(
        activity: Activity,
        trackAdapter: TrackAdapter,
        historyAdapter: TrackAdapter
    ): TrackSerachController {
        return TrackSerachController(activity, trackAdapter, historyAdapter)
    }

    fun getInfoController(activity: Activity): InfoController {
        return InfoController(activity)
    }
}