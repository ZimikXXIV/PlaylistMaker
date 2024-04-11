package com.example.playlistmaker.search.domain.api

interface TrackConsumer<T> {
    fun consume(data: ConsumerData<T>)
}