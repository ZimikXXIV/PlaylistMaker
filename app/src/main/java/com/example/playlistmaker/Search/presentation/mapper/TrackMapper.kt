package com.example.playlistmaker.Search.presentation.mapper

import com.example.playlistmaker.Search.data.dto.TrackDto
import com.example.playlistmaker.Search.domain.model.Track

object TrackMapper {
    fun map(tracksDto: List<TrackDto>): List<Track> {
        return tracksDto.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.primaryGenreName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                previewUrl = it.previewUrl
            )
        }
    }


}