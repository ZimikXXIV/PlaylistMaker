package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchTrackRepository {
    fun searchTrack(expression: String): Flow<ConsumerData<List<Track>>>
}