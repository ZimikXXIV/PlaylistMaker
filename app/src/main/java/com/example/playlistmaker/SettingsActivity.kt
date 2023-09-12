package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.security.AccessController.getContext


class SettingsActivity : AppCompatActivity() {

    private lateinit var textViewMedialibrary : TextView
    private lateinit var textViewShare : TextView
    private lateinit var textViewSupport : TextView
    private lateinit var textViewLicense : TextView
    private lateinit var switchDayNight : Switch
    @SuppressLint("IntentReset")
    fun init_settings() :Unit{
        textViewMedialibrary = findViewById<TextView>(R.id.btnBack)

        textViewMedialibrary.setOnClickListener{
            finish()
        }

        textViewShare = findViewById<TextView>(R.id.btnShare)

        textViewShare.setOnClickListener{
            val shareLink = getString(R.string.shareLink)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
            shareIntent.setType("text/plain")

            startActivity(shareIntent)
        }

        textViewSupport = findViewById<TextView>(R.id.btnSupport)

        textViewSupport.setOnClickListener{
            val subjectText = getString(R.string.mailSubject)
            val bodyText = getString(R.string.mailBody)
            val mailIntent = Intent(Intent.ACTION_SEND)

            mailIntent.data = Uri.parse("mailto:")
            mailIntent.setType("text/plain")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf( getString(R.string.mailDeveloper)))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText)
            mailIntent.putExtra(Intent.EXTRA_TEXT, bodyText)

            startActivity(mailIntent)
        }

        textViewLicense = findViewById<TextView>(R.id.btnLicense)

        textViewLicense.setOnClickListener{
            val linkText = getString(R.string.licenseLink)
            val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkText))

            startActivity(linkIntent)
        }

        switchDayNight = findViewById<Switch>(R.id.switchDayNight)

        val DarkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if(DarkModeFlags == Configuration.UI_MODE_NIGHT_YES)
        {
            switchDayNight.isChecked = true;
        }



        switchDayNight.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
    companion object {
        const val SWITCH_DAY_NIGHT = "SWITCH_DAY_NIGHT"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        init_settings()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("Save", "onSaveInstanceState")
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
        Log.d("Restore", "onRestoreInstanceState")
        if (savedInstanceState != null) {
            switchDayNight.isChecked = savedInstanceState.getBoolean(SWITCH_DAY_NIGHT, false)
        }
    }

}