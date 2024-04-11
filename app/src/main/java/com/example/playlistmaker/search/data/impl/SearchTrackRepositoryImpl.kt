package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.NetworkClient
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.mapper.TrackMapper
import com.example.playlistmaker.search.domain.model.Track

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient) : SearchTrackRepository {
    override fun searchTrack(expression: String): ConsumerData<List<Track>> {
        val response = networkClient.doRequest(SearchRequest(expression))
        return when (response.resultCode) {
            -1 -> ConsumerData.Error("Проверьте подключение к интернету")

            200 -> ConsumerData.Data(TrackMapper.map((response as SearchResponse).results))

            else -> {
                ConsumerData.Error("Ошибка сервера")
            }
        }
    }
}