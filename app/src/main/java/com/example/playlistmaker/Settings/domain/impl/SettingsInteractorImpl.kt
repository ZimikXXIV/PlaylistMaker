package com.example.playlistmaker.Settings.domain.impl

import com.example.playlistmaker.Settings.domain.api.SettingsInteractor
import com.example.playlistmaker.Settings.domain.api.SettingsRepository
import com.example.playlistmaker.Settings.domain.model.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}