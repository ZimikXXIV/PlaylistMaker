package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val packageIntent = this;
        val searchButton = findViewById<Button>(R.id.search)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(packageIntent, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        searchButton.setOnClickListener(imageClickListener);


        val medialibraryButton = findViewById<Button>(R.id.medialibrary)

        medialibraryButton.setOnClickListener{
            val intent = Intent(this, MedialibraryActivity::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<Button>(R.id.settings)

        settingsButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}