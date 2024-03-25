package com.example.playlistmaker.Search.domain.api

import com.example.playlistmaker.Search.domain.model.Track

interface SearchTrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
    }
}