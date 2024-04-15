package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.example.playlistmaker.search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.search.domain.model.Track

class HistoryTrackInteractorImpl(private val historyTrackRepository: HistoryTrackRepository) :
    HistoryTrackInteractor {

    override fun addToTrackHistoryWithSave(track: Track) {
        historyTrackRepository.addToTrackHistoryWithSave(track)
    }

    override fun saveTrackHistory() {
        historyTrackRepository.saveTrackHistory()
    }

    override fun loadTrackHistory(): ArrayList<Track> {
        return historyTrackRepository.loadTrackHistory()
    }

    override fun clearTrackHistory() {
        historyTrackRepository.clearTrackHistory()
    }
}