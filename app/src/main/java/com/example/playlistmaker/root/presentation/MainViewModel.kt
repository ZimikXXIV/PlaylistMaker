package com.example.playlistmaker.root.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsInteractor

class MainViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun loadTheme() {
        val theme = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(theme)
    }

}