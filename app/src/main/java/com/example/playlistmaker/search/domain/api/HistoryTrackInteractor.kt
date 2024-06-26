package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface HistoryTrackInteractor {
    fun addToTrackHistoryWithSave(track: Track)
    fun saveTrackHistory()
    fun loadTrackHistory(): ArrayList<Track>
    fun clearTrackHistory()
}