package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface SearchTrackInteractor {
    fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>>

}