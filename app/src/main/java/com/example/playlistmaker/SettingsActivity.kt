package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class SettingsActivity : AppCompatActivity() {

    private lateinit var textViewShare: TextView
    private lateinit var textViewSupport: TextView
    private lateinit var textViewLicense: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var switchDayNight: Switch

    fun initSettings() {
        btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        textViewShare = findViewById<TextView>(R.id.btnShare)

        textViewShare.setOnClickListener {
            val shareLink = getString(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
            shareIntent.type = "text/plain"

            startActivity(shareIntent)
        }

        textViewSupport = findViewById<TextView>(R.id.btnSupport)

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

        textViewLicense = findViewById<TextView>(R.id.btnLicense)

        textViewLicense.setOnClickListener {
            val linkText = getString(R.string.license_link)
            val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkText))

            startActivity(linkIntent)
        }

        switchDayNight = findViewById<Switch>(R.id.switchDayNight)

        val DarkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (DarkModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            switchDayNight.isChecked = true
        }



        switchDayNight.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initSettings()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(
            SWITCH_DAY_NIGHT,
            switchDayNight.isChecked
        )
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            switchDayNight.isChecked = savedInstanceState.getBoolean(SWITCH_DAY_NIGHT, false)
        }
    }

    companion object {
        const val SWITCH_DAY_NIGHT = "SWITCH_DAY_NIGHT"
    }

}