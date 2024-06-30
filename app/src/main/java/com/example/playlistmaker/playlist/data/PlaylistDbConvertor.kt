package com.example.playlistmaker.playlist.data


import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistTracksEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistWithTracks
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.utils.Utils

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistCaption = playlist.caption,
            playlistDescription = playlist.description,
            coverPath = playlist.coverPath
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            caption = playlist.playlistCaption,
            description = playlist.playlistDescription,
            coverPath = playlist.coverPath
        )
    }

    fun map(playlist: PlaylistWithTracks): PlaylistCard {
        return PlaylistCard(
            id = playlist.playlist.playlistId,
            caption = playlist.playlist.playlistCaption,
            description = playlist.playlist.playlistDescription,
            countTrack = playlist.playlistTrack.size,
            durationTrack = playlist.playlistTrack.sumOf { it.trackTimeMillis ?: 0L },
            coverImg = if (playlist.playlist.coverPath.isNullOrEmpty()) null else Uri.parse(playlist.playlist.coverPath),
            trackList = playlist.playlistTrack.map { track -> map(track) }
        )
    }

    fun map(track: PlaylistTracksEntity): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            trackTimeMillisStr = Utils.convertTimeToString(track.trackTimeMillis, "mm:ss"),
            artworkUrl100 = track.artworkUrl100,
            artworkUrl512 = Utils.getImageByResolution(track.artworkUrl100, "512x512"),
            previewUrl = track.previewUrl,
            dateAdd = track.dateAdd
        )
    }

    fun map(track: TrackInfo): PlaylistTracksEntity {
        return PlaylistTracksEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            dateAdd = null
        )
    }
}