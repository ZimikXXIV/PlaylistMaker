package com.example.playlistmaker.Main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.MediaLibrary.MediaLibraryActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.ui.SearchActivity
import com.example.playlistmaker.Settings.domain.api.SettingsInteractor
import com.example.playlistmaker.Settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {


    private lateinit var settingsIterator: SettingsInteractor
    fun init_form() {
        val packageIntent = this
        val searchButton = findViewById<Button>(R.id.btnSearch)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(packageIntent, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        searchButton.setOnClickListener(imageClickListener)


        val medialibraryButton = findViewById<Button>(R.id.btnMedialibrary)

        medialibraryButton.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<Button>(R.id.btnSettings)

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsIterator = Creator.getSettingsInteractor(this)

        init_form()

    }
}
