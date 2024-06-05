package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class SearchTrackInteractorImpl(private val repository: SearchTrackRepository) :
    SearchTrackInteractor {
    override fun searchTrack(expression: String): Flow<ConsumerData<List<Track>>> {
        return repository.searchTrack(expression)
    }
}
