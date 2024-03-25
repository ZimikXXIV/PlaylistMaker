package com.example.playlistmaker.Search.domain.api

import com.example.playlistmaker.Search.data.dto.NetworkResponse


interface NetworkClient {
    fun doRequest(dto: Any): NetworkResponse

}