package com.example.playlistmaker.Settings.domain.api

import com.example.playlistmaker.Settings.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareLink: String)
    fun openLink(openLink: String)
    fun openEmail(mail: EmailData)
}