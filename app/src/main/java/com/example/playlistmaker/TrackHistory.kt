package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson


class TrackHistory(private val sharedPrefsTrackHistory: SharedPreferences) {

    fun loadTrackHistory(): ArrayList<Track> {

        val jsonTrackHistory = sharedPrefsTrackHistory.getString(TRACK_HISTORY, null)

        if (jsonTrackHistory != null)
            return Gson().fromJson(jsonTrackHistory, Array<Track>::class.java)
                .toCollection(ArrayList<Track>())
        else
            return ArrayList()
    }

    fun saveTrackHistory(trackHistory: ArrayList<Track>) {
        val jsonTrackHistory = Gson().toJson(trackHistory)

        sharedPrefsTrackHistory.edit()
            .putString(TRACK_HISTORY, jsonTrackHistory)
            .apply()
    }

    fun addToTrackHistoryWithSave(track: Track, trackHistory: ArrayList<Track>) {

        trackHistory.removeIf { it.trackId == track.trackId }

        if (trackHistory.count() >= SearchActivity.MAX_SEARCH_COUNT) trackHistory.removeLast()

        trackHistory.add(0, track)

        saveTrackHistory(trackHistory)
    }

    companion object {
        const val TRACK_HISTORY = "track_history"
    }
}