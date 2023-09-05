package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("IntentReset")
    fun init_settings() :Unit{
        val btnMedialibrary = findViewById<TextView>(R.id.btnBack)

        btnMedialibrary.setOnClickListener{
            finish()
        }

        val btnShare = findViewById<TextView>(R.id.btnShare)

        btnShare.setOnClickListener{
            val shareText = "https://practicum.yandex.ru/profile/android-developer/"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            shareIntent.setType("text/plain")

            startActivity(shareIntent)
        }

        val btnSupport = findViewById<TextView>(R.id.btnSupport)

        btnSupport.setOnClickListener{
            val subjectText = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val bodyText = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val mailIntent = Intent(Intent.ACTION_SEND)

            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("Zimik24@yandex.ru"))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText)
            mailIntent.putExtra(Intent.EXTRA_TEXT, bodyText)
            mailIntent.setType("text/plain")

            startActivity(mailIntent)
        }

        val btnLicense = findViewById<TextView>(R.id.btnLicense)

        btnLicense.setOnClickListener{
            val linkText = "https://yandex.ru/legal/practicum_offer/"
            val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkText))

            startActivity(linkIntent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        init_settings()

    }
}