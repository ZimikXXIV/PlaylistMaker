package com.example.playlistmaker.Search.data

import com.example.playlistmaker.Search.data.dto.SearchRequest
import com.example.playlistmaker.Search.data.dto.SearchResponse
import com.example.playlistmaker.Search.domain.api.ConsumerData
import com.example.playlistmaker.Search.domain.api.NetworkClient
import com.example.playlistmaker.Search.domain.api.SearchTrackRepository
import com.example.playlistmaker.Search.domain.model.Track
import com.example.playlistmaker.Search.presentation.mapper.TrackMapper

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient) : SearchTrackRepository {
    override fun searchTrack(expression: String): ConsumerData<List<Track>> {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            return ConsumerData.Data(TrackMapper.map((response as SearchResponse).results))
        } else {
            return ConsumerData.Error("Ошибка соединения")
        }
    }
}