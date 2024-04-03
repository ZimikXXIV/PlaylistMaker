package com.example.playlistmaker.Search.domain.impl

import com.example.playlistmaker.Search.domain.api.HistoryTrackInteractor
import com.example.playlistmaker.Search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.Search.domain.model.Track

class HistoryTrackInteractorImpl(private val historyTrackRepository: HistoryTrackRepository) :
    HistoryTrackInteractor {

    override fun addToTrackHistoryWithSave(track: Track, trackHistory: ArrayList<Track>) {
        historyTrackRepository.addToTrackHistoryWithSave(track, trackHistory)
    }

    override fun saveTrackHistory(trackHistory: ArrayList<Track>) {
        historyTrackRepository.saveTrackHistory(trackHistory)
    }

    override fun loadTrackHistory(): ArrayList<Track> {
        return historyTrackRepository.loadTrackHistory()
    }

    override fun clearTrackHistory() {
        historyTrackRepository.clearTrackHistory()
    }
}