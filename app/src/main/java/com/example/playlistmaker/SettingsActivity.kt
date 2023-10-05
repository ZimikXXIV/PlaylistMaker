package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.DarkTheme.Companion.DARK_THEME
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private lateinit var textViewShare: TextView
    private lateinit var textViewSupport: TextView
    private lateinit var textViewLicense: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var switchDayNight: SwitchMaterial
    private lateinit var darkTheme: DarkTheme
    private fun setEvents() {

        btnBack.setOnClickListener {
            finish()
        }

        textViewShare.setOnClickListener {
            val shareLink = getString(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
            shareIntent.type = "text/plain"

            startActivity(shareIntent)
        }

        textViewSupport.setOnClickListener {
            val subjectText = getString(R.string.mail_subject)
            val bodyText = getString(R.string.mail_body)
            val mailIntent = Intent(Intent.ACTION_SENDTO)

            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_developer)))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText)
            mailIntent.putExtra(Intent.EXTRA_TEXT, bodyText)

            startActivity(mailIntent)
        }

        textViewLicense.setOnClickListener {
            val linkText = getString(R.string.license_link)
            val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkText))

            startActivity(linkIntent)
        }

        switchDayNight.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            darkTheme.darkTheme = checked
            darkTheme.saveDarkTheme()
        }
    }

    private fun initSettings() {
        btnBack = findViewById<ImageButton>(R.id.btnBack)
        textViewSupport = findViewById<TextView>(R.id.btnSupport)
        textViewShare = findViewById<TextView>(R.id.btnShare)
        textViewLicense = findViewById<TextView>(R.id.btnLicense)
        switchDayNight = findViewById<SwitchMaterial>(R.id.switchDayNight)

        darkTheme = DarkTheme(getSharedPreferences(DARK_THEME, MODE_PRIVATE))

        setEvents()

        darkTheme.loadDarkTheme()
        switchDayNight.isChecked = darkTheme.darkTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initSettings()

    }

}