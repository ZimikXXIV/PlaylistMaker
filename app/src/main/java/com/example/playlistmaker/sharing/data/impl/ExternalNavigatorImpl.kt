package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.model.EmailData
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun openEmail(mail: EmailData) {

        val mailIntent = Intent(Intent.ACTION_SENDTO)
        mailIntent.data = Uri.parse("mailto:")
        mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail.email))
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, mail.subject)
        mailIntent.putExtra(Intent.EXTRA_TEXT, mail.text)

        context.startActivity(mailIntent)
    }

    override fun openLink(openLink: String) {
        val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(openLink))
        context.startActivity(linkIntent)
    }

    override fun shareLink(shareLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
        shareIntent.type = "text/plain"

        context.startActivity(shareIntent)
    }

    override fun getShareAppLink(): String {
        return context.getString(R.string.share_link)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            subject = context.getString(R.string.mail_subject),
            text = context.getString(R.string.mail_body),
            email = context.getString(R.string.mail_developer)
        )
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.license_link)
    }

}