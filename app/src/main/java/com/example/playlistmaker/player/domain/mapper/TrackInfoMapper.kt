package com.example.playlistmaker.player.domain.mapper


import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils

object TrackInfoMapper {
    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.releaseDate.toString().take(4),
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl512 = Utils.getIamgeByResolution(track.artworkUrl100, "512x512"),
            previewUrl = track.previewUrl
        )
    }
}