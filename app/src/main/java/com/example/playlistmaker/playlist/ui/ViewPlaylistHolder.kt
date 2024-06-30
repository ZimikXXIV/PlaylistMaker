package com.example.playlistmaker.playlist.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.model.TrackInfo
import com.example.playlistmaker.utils.Utils


class ViewPlaylistHolder(
    itemView: View,
    private val onLongClick: (input: TrackInfo) -> Unit,
    private val onClick: (input: TrackInfo) -> Unit
) :
    RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val duration: TextView = itemView.findViewById(R.id.durationTextView)
    private val cover: ImageView = itemView.findViewById(R.id.coverAlbum)

    fun bind(track: TrackInfo) {

        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text = Utils.convertTimeToString(track.trackTimeMillis, "mm:ss")

        itemView.setOnLongClickListener {
            onLongClick(track)
            return@setOnLongClickListener true
        }

        itemView.setOnClickListener {
            onClick(track)
        }

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder_ico)
            .transform(CenterCrop(), RoundedCorners(Utils.dpToPx(8f)))
            .into(cover)

        artistName.requestLayout()

    }

}
