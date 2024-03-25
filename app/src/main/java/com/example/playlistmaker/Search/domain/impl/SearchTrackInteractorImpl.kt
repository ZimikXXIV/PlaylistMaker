package com.example.playlistmaker.Search.domain.impl

import com.example.playlistmaker.Search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.Search.domain.api.SearchTrackRepository

class SearchTrackInteractorImpl(private val repository: SearchTrackRepository) :
    SearchTrackInteractor {
    override fun searchTrack(expression: String, consumer: SearchTrackInteractor.TrackConsumer) {
        val t = Thread {
            consumer.consume(repository.searchTrack(expression))
        }
        t.start()
    }
}