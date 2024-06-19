package com.example.playlistmaker.medialibrary.data.db

import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils

class TrackDbConvertor {
    fun map(track: TrackInfo): TrackEntity {
        return TrackEntity(
            Id = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            dateADD = null
        )
    }

    fun map(track: TrackEntity): TrackInfo {
        return TrackInfo(
            trackId = track.Id,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl512 = Utils.getIamgeByResolution(track.artworkUrl100, "512x512"),
            previewUrl = track.previewUrl
        )
    }

    fun mapToTrack(track: TrackEntity): Track {
        return Track(
            trackId = track.Id,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl
        )
    }
}
