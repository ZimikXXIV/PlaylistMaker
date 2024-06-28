package com.example.playlistmaker.player.domain.mapper


import com.example.playlistmaker.player.domain.model.TrackInfo
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
            trackTimeMillisStr = Utils.convertTimeToString(track.trackTimeMillis, "mm:ss"),
            artworkUrl100 = track.artworkUrl100,
            artworkUrl512 = Utils.getImageByResolution(track.artworkUrl100, "512x512"),
            previewUrl = track.previewUrl
        )
    }

    fun map(track: TrackInfo): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.releaseDate.toString().take(4),
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl
        )
    }
}