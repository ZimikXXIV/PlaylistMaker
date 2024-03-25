package com.example.playlistmaker.Search.data

import com.example.playlistmaker.Search.data.dto.SearchRequest
import com.example.playlistmaker.Search.data.dto.SearchResponse
import com.example.playlistmaker.Search.data.mapper.TrackMapper
import com.example.playlistmaker.Search.domain.api.NetworkClient
import com.example.playlistmaker.Search.domain.api.SearchTrackRepository
import com.example.playlistmaker.Search.domain.model.Track

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient) : SearchTrackRepository {
    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            return TrackMapper.map((response as SearchResponse).results)
        } else {
            return emptyList()
        }
    }
}