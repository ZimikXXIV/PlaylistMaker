package com.example.playlistmaker.Search.domain.api

sealed class ConsumerData<T>(val data: T? = null, val message: String? = null) {
    class Data<T>(data: T) : ConsumerData<T>(data, null)
    class Error<T>(message: String) : ConsumerData<T>(null, message)
}

