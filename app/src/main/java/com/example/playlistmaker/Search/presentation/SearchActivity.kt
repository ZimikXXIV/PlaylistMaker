package com.example.playlistmaker.Search.presentation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Search.domain.api.TrackListClickListenerInterface
import com.example.playlistmaker.Search.domain.model.Track


class SearchActivity : AppCompatActivity(), TrackListClickListenerInterface {

    private val trackAdapter = TrackAdapter(this)
    private val historyAdapter = TrackAdapter(this)

    private lateinit var trackSearchController: TrackSerachController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        trackSearchController = Creator.getTrackSearchController(this, trackAdapter, historyAdapter)
        trackSearchController.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        trackSearchController.onDestroy()
    }
    override fun onStop() {
        super.onStop()
        trackSearchController.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        trackSearchController.onSaveInstanceState(outState)
    }

    override fun onClick(track: Track) {
        trackSearchController.onClick(track)
    }
    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        trackSearchController.onRestoreInstanceState(savedInstanceState, persistentState)
    }


}
