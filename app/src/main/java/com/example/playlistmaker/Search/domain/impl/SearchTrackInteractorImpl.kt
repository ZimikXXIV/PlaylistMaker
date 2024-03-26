package com.example.playlistmaker.Search.domain.impl

import com.example.playlistmaker.Search.domain.api.ConsumerData
import com.example.playlistmaker.Search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.Search.domain.api.SearchTrackRepository
import com.example.playlistmaker.Search.domain.api.TrackConsumer
import com.example.playlistmaker.Search.domain.model.Track

class SearchTrackInteractorImpl(private val repository: SearchTrackRepository) :
    SearchTrackInteractor {
    override fun searchTrack(expression: String, consumer: TrackConsumer<List<Track>>) {
        val t = Thread {
            //consumer.consume(repository.searchTrack(expression))
            when (val consumedData = repository.searchTrack(expression)) {
                is ConsumerData.Data -> {
                    consumer.consume(consumedData)
                }

                is ConsumerData.Error -> {
                    consumer.consume(consumedData)
                }
            }
        }
        t.start()
    }
}