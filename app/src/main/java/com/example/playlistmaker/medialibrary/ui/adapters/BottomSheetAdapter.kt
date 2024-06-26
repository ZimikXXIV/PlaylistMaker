package com.example.playlistmaker.medialibrary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.medialibrary.domain.api.PlaylistSheetClickListenerInterface

class BottomSheetAdapter(
    private val onClick: PlaylistSheetClickListenerInterface?
) : RecyclerView.Adapter<BottomSheetHolder>() {

    private var playlistList: List<PlaylistCard> = emptyList()

    fun setPlaylistList(playlists: List<PlaylistCard>) {
        playlistList = playlists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_list, parent, false)
        return if (onClick != null)
            BottomSheetHolder(view, onClick)
        else
            BottomSheetHolder(view, null)
    }

    override fun onBindViewHolder(holder: BottomSheetHolder, position: Int) {
        holder.bind(playlistList[position])
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }
}

