package com.example.playlistmaker.settings.data.impl


import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.google.gson.Gson

class SettingsRepositoryImpl(
    private val context: Context,
    private val sharedPrefs: SharedPreferences
) : SettingsRepository {

    private var darkTheme = ThemeSettings(false)
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

        darkTheme = if (!jsonSettings.isNullOrEmpty()) {
            Gson().fromJson(jsonSettings, ThemeSettings::class.java)
        } else {
            val darkMode =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            ThemeSettings(darkMode == Configuration.UI_MODE_NIGHT_YES)
        }

        updateThemeSetting(darkTheme)
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