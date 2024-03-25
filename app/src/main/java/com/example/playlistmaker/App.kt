package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false
    var debounce = Debounce
    override fun onCreate() {
        super.onCreate()
        val darkTheme = DarkTheme(getSharedPreferences(DarkTheme.DARK_THEME, MODE_PRIVATE))
        darkTheme.loadDarkTheme()
        switchTheme(darkTheme.darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }


}
