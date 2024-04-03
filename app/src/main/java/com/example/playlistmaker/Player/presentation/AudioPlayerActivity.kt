package com.example.playlistmaker.Player.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Creator.Creator


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var infoController: InfoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        infoController = Creator.getInfoController(this)
        infoController.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        infoController.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        infoController.onDestroy()
    }

}