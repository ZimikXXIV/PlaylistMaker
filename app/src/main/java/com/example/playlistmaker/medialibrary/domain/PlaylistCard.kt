package com.example.playlistmaker.medialibrary.domain

import android.net.Uri
import com.example.playlistmaker.player.domain.model.TrackInfo

data class PlaylistCard(
    val id: Int,
    val caption: String,
    val description: String?,
    val coverImg: Uri?,
    val countTrack: Int,
    val durationTrack: Long,
    val trackList: List<TrackInfo>
)
