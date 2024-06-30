package com.example.playlistmaker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.api.TrackListClickListenerInterface
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils


class TrackHolder(itemView: View, private val onClick: TrackListClickListenerInterface?) :
    RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val duration: TextView = itemView.findViewById(R.id.durationTextView)
    private val cover: ImageView = itemView.findViewById(R.id.coverAlbum)

    fun bind(track: Track) {

        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text = Utils.convertTimeToString(track.trackTimeMillis, "mm:ss")

        itemView.setOnClickListener {
            onClick?.onClick(track)


        }

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder_ico)
            .transform(RoundedCorners(Utils.dpToPx(2f)))
            .centerCrop()
            .into(cover)

        artistName.requestLayout()

    }
}
