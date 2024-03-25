package com.example.playlistmaker.Search.domain.api

import com.example.playlistmaker.Search.domain.model.Track

interface SearchTrackRepository {
    fun searchTrack(expression: String): List<Track>
}