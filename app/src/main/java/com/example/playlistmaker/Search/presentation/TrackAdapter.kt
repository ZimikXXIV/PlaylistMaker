package com.example.playlistmaker.Search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.data.repository.TrackListClickListenerInterface
import com.example.playlistmaker.Search.domain.model.Track

class TrackAdapter(
    private val tracks: List<Track>,
    private val onClick: TrackListClickListenerInterface?
) : RecyclerView.Adapter<TrackHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list, parent, false)
        if (onClick != null)
            return TrackHolder(view, onClick)
        else
            return TrackHolder(view, null)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}
