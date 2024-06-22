package com.example.playlistmaker.playlist.data


import android.net.Uri
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.playlist.data.Entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.Entity.PlaylistWithTracksEntity
import com.example.playlistmaker.playlist.domain.model.Playlist

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
            caption = playlist.playlist.playlistCaption,
            countTrack = playlist.playlistTrack.size,
            coverImg = if (playlist.playlist.coverPath.isNullOrEmpty()) null else Uri.parse(playlist.playlist.coverPath)
        )
    }
}