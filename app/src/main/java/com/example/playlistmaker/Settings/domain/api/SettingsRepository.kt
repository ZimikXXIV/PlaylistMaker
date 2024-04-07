package com.example.playlistmaker.Settings.domain.api

import com.example.playlistmaker.Settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}