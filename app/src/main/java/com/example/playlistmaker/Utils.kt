package com.example.playlistmaker

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
}