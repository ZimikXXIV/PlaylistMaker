package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchTrackRepository {
    fun searchTrack(expression: String): ConsumerData<List<Track>>
}