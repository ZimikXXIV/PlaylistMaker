package com.example.playlistmaker

import android.content.SharedPreferences

class DarkTheme(private val sharedPrefsDarkTheme: SharedPreferences) {

    var darkTheme = false

    fun loadDarkTheme() {
        darkTheme = sharedPrefsDarkTheme.getBoolean(DARK_THEME, false)
    }

    fun saveDarkTheme() {
        sharedPrefsDarkTheme.edit()
            .putBoolean(DARK_THEME, darkTheme)
            .apply()
    }

    companion object {
        const val DARK_THEME = "dark_theme"
    }
}