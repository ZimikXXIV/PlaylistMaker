package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.dto.NetworkResponse
import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.domain.api.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitClient(
    private val iTunesApi: iTunesApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResponse {
        if (!isConnected()) {
            return NetworkResponse().apply { resultCode = -1 }
        }

        if (dto !is SearchRequest) {
            return NetworkResponse().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val resp = iTunesApi.searchTrack(dto.expression)

                resp.apply { resultCode = 200 }
            } catch (ex: Throwable) {
                NetworkResponse().apply { resultCode = 500 }
            }
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
