package com.example.playlistmaker.utils

import android.content.res.Resources
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    const val CLICK_DEBOUNCE_DELAY = 1000L

    fun convertTimeToString(trackTime: Long?, timeFormat: String): String {
        return SimpleDateFormat(
            timeFormat,
            Locale.getDefault()
        ).format(trackTime!!)
    }

    fun getImageByResolution(previewUrl: String?, resolution: String): String? {
        return previewUrl?.replaceAfterLast('/', "${resolution}bb.jpg")
    }

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            Resources.getSystem().displayMetrics
        ).toInt()
    }

}
