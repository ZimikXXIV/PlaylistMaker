package com.example.playlistmaker.Settings.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Settings.domain.api.SettingsInteractor
import com.example.playlistmaker.Settings.domain.api.SharingInteractor
import com.example.playlistmaker.Settings.domain.model.ThemeSettings
import com.example.playlistmaker.Settings.presentation.model.SettingsState

class SettingViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var loadingLiveData = MutableLiveData<SettingsState>()
    private var darkThemeSettings = ThemeSettings(false)
    fun getSettingsState(): LiveData<SettingsState> = loadingLiveData

    fun loadTheme() {
        val theme = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(theme)
        loadingLiveData.postValue(SettingsState(theme.isDarkTheme))
    }

    fun switchTheme(isDarkTheme: Boolean) {
        val themeSettings = ThemeSettings(isDarkTheme)
        if (themeSettings != darkThemeSettings) {
            settingsInteractor.updateThemeSetting(themeSettings)
            darkThemeSettings = themeSettings
            loadingLiveData.postValue(SettingsState(isDarkTheme))
        }
    }

    fun shareLink() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun sendMail() {
        sharingInteractor.openSupport()
    }

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingViewModel(
                        Creator.getSharingInteractor(context),
                        Creator.getSettingsInteractor(context)
                    )
                }
            }
    }

}