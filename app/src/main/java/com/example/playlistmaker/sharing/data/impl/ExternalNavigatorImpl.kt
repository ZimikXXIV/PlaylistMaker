package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.model.EmailData
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun openEmail() {
        val mail = getSupportEmailData()
        val mailIntent = Intent(Intent.ACTION_SENDTO)
        mailIntent.data = Uri.parse("mailto:")
        mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail.email))
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, mail.subject)
        mailIntent.putExtra(Intent.EXTRA_TEXT, mail.text)
        mailIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(mailIntent)
    }

    override fun openLink() {
        val openLink = getTermsLink()
        val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(openLink))
        linkIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(linkIntent)
    }

    override fun shareLink() {
        val shareLink = getShareAppLink()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
        shareIntent.type = "text/plain"
        shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(shareIntent)
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

    override fun shareString(shareText: String) {
        val shareString = Intent(Intent.ACTION_SEND)
        shareString.type = "text/plain"
        shareString.putExtra(Intent.EXTRA_TEXT, shareText)
        shareString.addFlags(FLAG_ACTIVITY_NEW_TASK)

        val chooserIntent = Intent.createChooser(shareString, null)
        chooserIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)

        startActivity(context, chooserIntent, null)
    }

}