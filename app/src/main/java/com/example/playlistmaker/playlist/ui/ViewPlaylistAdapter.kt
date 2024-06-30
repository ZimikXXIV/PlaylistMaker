package com.example.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.model.TrackInfo

class ViewPlaylistAdapter(
    private val onLongClick: (input: TrackInfo) -> Unit,
    private val onClick: (input: TrackInfo) -> Unit
) : RecyclerView.Adapter<ViewPlaylistHolder>() {

    private var trackList: List<TrackInfo> = emptyList()
    fun setTrackList(tracks: List<TrackInfo>) {
        trackList = tracks.sortedByDescending { track -> track.dateAdd }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPlaylistHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list, parent, false)
        return ViewPlaylistHolder(view, onLongClick, onClick)
    }

    override fun onBindViewHolder(holder: ViewPlaylistHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}

