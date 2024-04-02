package com.example.playlistmaker

import android.os.Handler
import android.os.Looper

object Debounce {


    private const val CLICK_DEBOUNCE_DELAY = 1000L
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun debounce(runnable: Runnable, debounceDelay: Long) {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, debounceDelay)
    }

    fun removeCallbacks(runnable: Runnable) {
        handler.removeCallbacks(runnable)
    }

}