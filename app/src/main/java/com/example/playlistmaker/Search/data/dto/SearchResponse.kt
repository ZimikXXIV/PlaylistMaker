package com.example.playlistmaker.Search.data.dto

data class SearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : NetworkResponse()

