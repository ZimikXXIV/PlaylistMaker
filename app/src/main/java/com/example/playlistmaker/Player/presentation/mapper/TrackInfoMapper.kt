package com.example.playlistmaker.Player.presentation.mapper


import com.example.playlistmaker.Player.presentation.model.TrackInfo
import com.example.playlistmaker.Search.domain.model.Track
import com.example.playlistmaker.Utils

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
            trackTimeMillis = Utils.convertTimeToString(track.trackTimeMillis, "mm:ss"),
            artworkUrl512 = Utils.getIamgeByResolution(track.artworkUrl100, "512"),
            previewUrl = track.previewUrl
        )
    }
}