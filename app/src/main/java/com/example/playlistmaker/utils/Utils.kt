package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun convertTimeToString(trackTime: String?, timeFormat: String): String {
        return SimpleDateFormat(
            timeFormat,
            Locale.getDefault()
        ).format(trackTime!!.toLong())
    }

    fun getIamgeByResolution(previewUrl: String?, resolution: String): String? {
        return previewUrl?.replaceAfterLast('/', "${resolution}bb.jpg")
    }

    fun getStringCountTracks(trackCount: Int): String {
        return when (trackCount) {
            1 -> "трек"
            2, 3, 4 -> "трека"
            else -> "треков"
        }
    }

}