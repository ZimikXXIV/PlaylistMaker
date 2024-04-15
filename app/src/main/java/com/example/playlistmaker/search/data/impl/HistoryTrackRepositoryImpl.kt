package com.example.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson


class HistoryTrackRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) :
    HistoryTrackRepository {

    private var trackHistory: ArrayList<Track> = ArrayList()
    override fun addToTrackHistoryWithSave(track: Track) {

        trackHistory.removeIf { it.trackId == track.trackId }

        if (trackHistory.count() >= SearchConst.MAX_SEARCH_COUNT) trackHistory.removeLast()

        trackHistory.add(0, track)

        saveTrackHistory()
    }

    override fun saveTrackHistory() {
        val jsonTrackHistory = gson.toJson(trackHistory)

        sharedPrefs.edit()
            .putString(TRACK_HISTORY, jsonTrackHistory)
            .apply()
    }

    override fun loadTrackHistory(): ArrayList<Track> {

        val jsonTrackHistory = sharedPrefs.getString(TRACK_HISTORY, null)

        trackHistory = if (jsonTrackHistory != null)
            gson.fromJson(jsonTrackHistory, Array<Track>::class.java)
                .toCollection(ArrayList<Track>())
        else
            ArrayList()
        return trackHistory
    }

    override fun clearTrackHistory() {
        trackHistory.clear()
        sharedPrefs.edit().clear().apply()
    }

    companion object {
        const val TRACK_HISTORY = "track_history"
        const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_history"
    }
}
