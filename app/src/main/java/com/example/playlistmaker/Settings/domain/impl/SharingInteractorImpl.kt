package com.example.playlistmaker.Settings.domain.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.Settings.domain.api.ExternalNavigator
import com.example.playlistmaker.Settings.domain.api.SharingInteractor
import com.example.playlistmaker.Settings.domain.model.EmailData

class SharingInteractorImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.share_link)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            subject = context.getString(R.string.mail_subject),
            text = context.getString(R.string.mail_body),
            email = context.getString(R.string.mail_developer)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.license_link)
    }
}