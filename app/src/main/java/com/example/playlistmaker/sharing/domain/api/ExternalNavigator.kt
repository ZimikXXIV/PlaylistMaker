package com.example.playlistmaker.sharing.domain.api

import com.example.playlistmaker.settings.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareLink: String)
    fun openLink(openLink: String)
    fun openEmail(mail: EmailData)
    fun getShareAppLink(): String
    fun getSupportEmailData(): EmailData
    fun getTermsLink(): String

}