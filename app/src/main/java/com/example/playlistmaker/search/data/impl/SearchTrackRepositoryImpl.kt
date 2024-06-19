package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.NetworkClient
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.mapper.TrackMapper
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient) : SearchTrackRepository {
    override fun searchTrack(expression: String): Flow<ConsumerData<List<Track>>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(ConsumerData.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                emit(ConsumerData.Data(TrackMapper.map((response as SearchResponse).results)))
            }

            else -> {
                emit(ConsumerData.Error("Ошибка сервера"))
            }
        }
    }
}
