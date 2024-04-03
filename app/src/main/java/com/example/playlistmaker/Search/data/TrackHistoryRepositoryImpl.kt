package com.example.playlistmaker.Search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.Search.domain.api.HistoryTrackRepository
import com.example.playlistmaker.Search.domain.model.SearchConst
import com.example.playlistmaker.Search.domain.model.Track
import com.google.gson.Gson


class TrackHistoryRepositoryImpl(context: Context) :
    HistoryTrackRepository {
    private val sharedPrefs = context.getSharedPreferences(TRACK_HISTORY, MODE_PRIVATE)
    override fun addToTrackHistoryWithSave(track: Track, trackHistory: ArrayList<Track>) {

        trackHistory.removeIf { it.trackId == track.trackId }

        if (trackHistory.count() >= SearchConst.MAX_SEARCH_COUNT) trackHistory.removeLast()

        trackHistory.add(0, track)

        saveTrackHistory(trackHistory)
    }

    override fun saveTrackHistory(trackHistory: ArrayList<Track>) {
        val jsonTrackHistory = Gson().toJson(trackHistory)

        sharedPrefs.edit()
            .putString(TRACK_HISTORY, jsonTrackHistory)
            .apply()
    }

    override fun loadTrackHistory(): ArrayList<Track> {

        val jsonTrackHistory = sharedPrefs.getString(TRACK_HISTORY, null)

        if (jsonTrackHistory != null)
            return Gson().fromJson(jsonTrackHistory, Array<Track>::class.java)
                .toCollection(ArrayList<Track>())
        else
            return ArrayList()
    }

    override fun clearTrackHistory() {
        sharedPrefs.edit().remove(TRACK_HISTORY).apply()
    }

    companion object {
        const val TRACK_HISTORY = "track_history"
    }
}
