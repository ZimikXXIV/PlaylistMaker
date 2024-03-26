package com.example.playlistmaker.Search.data.network

import com.example.playlistmaker.Search.data.dto.NetworkResponse
import com.example.playlistmaker.Search.data.dto.SearchRequest
import com.example.playlistmaker.Search.domain.api.NetworkClient
import com.example.playlistmaker.Search.domain.model.SearchConst
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient : NetworkClient {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SearchConst.ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val iTunesService = retrofit.create(iTunesApi::class.java)

    override fun doRequest(dto: Any): NetworkResponse {
        if (dto is SearchRequest) {
            val resp = iTunesService.searchTrack(dto.expression).execute()

            val body = resp.body() ?: NetworkResponse()

            return body.apply { resultCode = resp.code() }
        } else {
            return NetworkResponse().apply { resultCode = 400 }
        }
    }
}
