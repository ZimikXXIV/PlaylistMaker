package com.example.playlistmaker.search.data.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson


class TrackHistoryRepositoryImpl(context: Context) :
    HistoryTrackRepository {
    private val sharedPrefs = context.getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)

    private var trackHistory: ArrayList<Track> = ArrayList()
    override fun addToTrackHistoryWithSave(track: Track) {

        trackHistory.removeIf { it.trackId == track.trackId }

        if (trackHistory.count() >= SearchConst.MAX_SEARCH_COUNT) trackHistory.removeLast()

        trackHistory.add(0, track)

        saveTrackHistory()
    }

    override fun saveTrackHistory() {
        val jsonTrackHistory = Gson().toJson(trackHistory)

        sharedPrefs.edit()
            .putString(TRACK_HISTORY, jsonTrackHistory)
            .apply()
    }

    override fun loadTrackHistory(): ArrayList<Track> {

        val jsonTrackHistory = sharedPrefs.getString(TRACK_HISTORY, null)

        trackHistory = if (jsonTrackHistory != null)
            Gson().fromJson(jsonTrackHistory, Array<Track>::class.java)
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
