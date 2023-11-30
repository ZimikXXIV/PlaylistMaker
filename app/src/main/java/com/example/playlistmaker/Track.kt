package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val previewUrl: String?
) : Parcelable {
    fun convertTimeToString(timeFormat: String): String {
        return SimpleDateFormat(
            timeFormat,
            Locale.getDefault()
        ).format(this.trackTimeMillis!!.toLong())
    }

    fun getIamgeByResolution(resolution: String): String? {
        return this.artworkUrl100?.replaceAfterLast('/', "${resolution}bb.jpg")
    }

}








