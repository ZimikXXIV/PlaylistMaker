package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track


interface SearchTrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer<List<Track>>)

}