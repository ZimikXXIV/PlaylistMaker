package com.example.playlistmaker.Settings.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.Settings.domain.api.ExternalNavigator
import com.example.playlistmaker.Settings.domain.model.EmailData

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
}