package com.example.playlistmaker.Search.domain.api

import com.example.playlistmaker.Search.domain.model.Track

interface HistoryTrackRepository {
    fun addToTrackHistoryWithSave(track: Track, trackHistory: ArrayList<Track>)
    fun saveTrackHistory(trackHistory: ArrayList<Track>)
    fun loadTrackHistory(): ArrayList<Track>
    fun clearTrackHistory()

}
