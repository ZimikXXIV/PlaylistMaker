package com.example.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.playlist.domain.api.TrackInfoClickListenerInterface
import com.example.playlistmaker.playlist.domain.api.TrackInfoLongClickListenerInterface

class ViewPlaylistAdapter(
    private val onLongClick: TrackInfoLongClickListenerInterface?,
    private val onClick: TrackInfoClickListenerInterface?
) : RecyclerView.Adapter<ViewPlaylistHolder>() {

    private var trackList: List<TrackInfo> = emptyList()
    fun setTrackList(tracks: List<TrackInfo>) {
        trackList = tracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPlaylistHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list, parent, false)
        return if (onLongClick != null && onClick != null)
            ViewPlaylistHolder(view, onLongClick, onClick)
        else
            ViewPlaylistHolder(view, null, null)
    }

    override fun onBindViewHolder(holder: ViewPlaylistHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}

