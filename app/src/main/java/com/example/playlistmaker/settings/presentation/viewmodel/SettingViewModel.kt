package com.example.playlistmaker.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.presentation.model.SettingsState
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var loadingLiveData = MutableLiveData<SettingsState>()
    private var darkThemeSettings = settingsInteractor.getThemeSettings()
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
}
