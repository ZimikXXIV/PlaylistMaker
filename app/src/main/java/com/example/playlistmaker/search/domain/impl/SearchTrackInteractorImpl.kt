package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTrackInteractorImpl(private val repository: SearchTrackRepository) :
    SearchTrackInteractor {
    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTrack(expression).map { result ->
            when (result) {
                is ConsumerData.Data -> {
                    Pair(result.data, null)
                }

                is ConsumerData.Error -> {
                    Pair(null, result.errorMessage)
                }
            }
        }
    }
}
