package com.example.playlistmaker.Search.domain.api

interface TrackConsumer<T> {
    fun consume(data: ConsumerData<T>)
}