package com.example.playlistmaker.Settings.domain.api

import com.example.playlistmaker.Settings.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}