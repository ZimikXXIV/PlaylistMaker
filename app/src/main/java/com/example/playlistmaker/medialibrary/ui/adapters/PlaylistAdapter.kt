package com.example.playlistmaker.medialibrary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.PlaylistCard

class PlaylistAdapter(
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    private var playlistCards: List<PlaylistCard> = emptyList()
    fun setPlaylistCards(playlists: List<PlaylistCard>) {
        playlistCards = playlists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_card, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlistCards[position])
    }

    override fun getItemCount(): Int {
        return playlistCards.size
    }
}

