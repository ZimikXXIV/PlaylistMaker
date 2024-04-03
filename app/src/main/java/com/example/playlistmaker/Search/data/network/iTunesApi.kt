package com.example.playlistmaker.Search.data.network

import com.example.playlistmaker.Search.data.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<SearchResponse>
}