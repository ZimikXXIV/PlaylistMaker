package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.player.domain.model.PlayerStatus
import com.example.playlistmaker.search.data.impl.HistoryTrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.data.network.iTunesApi
import com.example.playlistmaker.search.domain.api.NetworkClient
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.presentation.model.SettingsState
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<iTunesApi> {
        Retrofit.Builder()
            .baseUrl(SearchConst.ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApi::class.java)
    }

    single(named(SearchConst.SHAREDPREFERENCES_TAG)) {
        androidContext()
            .getSharedPreferences(SearchConst.SHAREDPREFERENCES_TAG, Context.MODE_PRIVATE)
    }

    single(named(HistoryTrackRepositoryImpl.PLAYLISTMAKER_PREFERENCES)) {
        androidContext()
            .getSharedPreferences(
                HistoryTrackRepositoryImpl.PLAYLISTMAKER_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitClient(get(), androidContext())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single<PlayerStatus> {
        PlayerStatus.DEFAULT
    }

    single<ThemeSettings> {
        ThemeSettings(false)
    }

    single<MutableLiveData<SettingsState>> {
        MutableLiveData<SettingsState>()
    }

}