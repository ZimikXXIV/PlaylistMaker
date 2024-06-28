package com.example.playlistmaker.sharing.domain.api

interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
    fun shareString(shareText: String)
}