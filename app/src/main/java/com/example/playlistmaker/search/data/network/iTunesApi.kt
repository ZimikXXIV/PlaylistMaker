package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): SearchResponse
}