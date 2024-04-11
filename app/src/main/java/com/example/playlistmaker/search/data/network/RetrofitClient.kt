package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.dto.NetworkResponse
import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.domain.api.NetworkClient
import com.example.playlistmaker.search.domain.model.SearchConst
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(private val context: Context) : NetworkClient {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SearchConst.ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val iTunesService = retrofit.create(iTunesApi::class.java)

    override fun doRequest(dto: Any): NetworkResponse {
        if (isConnected() == false) {
            return NetworkResponse().apply { resultCode = -1 }
        }
        return try {
            if (dto is SearchRequest) {
                val resp = iTunesService.searchTrack(dto.expression).execute()

                val body = resp.body() ?: NetworkResponse()

                body.apply { resultCode = resp.code() }
            } else {
                NetworkResponse().apply { resultCode = 400 }
            }
        } catch (ex: Exception) {
            NetworkResponse().apply { resultCode = -1 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}