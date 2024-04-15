package com.example.playlistmaker.search.domain.api

sealed interface ConsumerData<T> {
    data class Data<T>(val data: T) : ConsumerData<T>
    data class Error<T>(val errorMessage: String) : ConsumerData<T>
}

