package com.example.playlistmaker.playlist.data


import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistTracksEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistWithTracksEntity
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.utils.Utils

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistCaption = playlist.caption,
            playlistDescription = playlist.description,
            coverPath = playlist.coverPath
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            caption = playlist.playlistCaption,
            description = playlist.playlistDescription,
            coverPath = playlist.coverPath
        )
    }

    fun map(playlist: PlaylistWithTracksEntity): PlaylistCard {
        return PlaylistCard(
            id = playlist.playlist.playlistId,
            caption = playlist.playlist.playlistCaption,
            countTrack = playlist.playlistTrack.size,
            coverImg = if (playlist.playlist.coverPath.isNullOrEmpty()) null else Uri.parse(playlist.playlist.coverPath),
            trackList = playlist.playlistTrack.map { track -> map(track) }
        )
    }

    fun map(track: PlaylistTracksEntity): TrackInfo {
        return TrackInfo(
            trackId = track.trackAppleId,
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

    fun map(track: TrackInfo, playlist: PlaylistCard): PlaylistTracksEntity {
        return PlaylistTracksEntity(
            trackId = 0,
            trackAppleId = track.trackId,
            fkPlaylistId = playlist.id,
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
}