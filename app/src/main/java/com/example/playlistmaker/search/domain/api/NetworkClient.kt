package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.dto.NetworkResponse


interface NetworkClient {
    fun doRequest(dto: Any): NetworkResponse

}