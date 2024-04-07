package com.example.playlistmaker.Settings.data.impl

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Settings.domain.api.SettingsRepository
import com.example.playlistmaker.Settings.domain.model.ThemeSettings
import com.google.gson.Gson

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val sharedPrefs =
        context.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)

    private var darkTheme: ThemeSettings = ThemeSettings(false)

    init {
        loadDarkTheme()
    }

    override fun getThemeSettings(): ThemeSettings {
        return darkTheme
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        darkTheme = settings
        switchTheme()
        saveThemeSettings()
    }

    fun loadDarkTheme() {
        val jsonSettings = sharedPrefs.getString(THEME_SETTINGS, null)

        if (!jsonSettings.isNullOrEmpty()) {
            darkTheme = Gson().fromJson(jsonSettings, ThemeSettings::class.java)
        }
        switchTheme()
    }

    private fun saveThemeSettings() {
        val jsonString = Gson().toJson(darkTheme)

        sharedPrefs.edit()
            .putString(THEME_SETTINGS, jsonString)
            .apply()
    }

    private fun switchTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme.isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val THEME_SETTINGS = "THEME_SETTINGS"
        const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
    }
}