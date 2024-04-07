package com.example.playlistmaker.Search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.domain.api.TrackListClickListenerInterface
import com.example.playlistmaker.Search.domain.model.Track

class TrackAdapter(
    private val onClick: TrackListClickListenerInterface?
) : RecyclerView.Adapter<TrackHolder>() {

    private var trackList: List<Track> = emptyList()
    fun setTrackList(tracks: List<Track>) {
        trackList = tracks
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list, parent, false)
        return if (onClick != null)
            TrackHolder(view, onClick)
        else
            TrackHolder(view, null)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}

