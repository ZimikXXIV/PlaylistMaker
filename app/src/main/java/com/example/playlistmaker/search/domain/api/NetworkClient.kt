package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.dto.NetworkResponse


interface NetworkClient {
    suspend fun doRequest(dto: Any): NetworkResponse

}