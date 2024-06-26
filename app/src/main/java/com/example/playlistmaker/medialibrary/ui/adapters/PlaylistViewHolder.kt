package com.example.playlistmaker.medialibrary.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.PlaylistCard
import com.example.playlistmaker.search.ui.TrackHolder


class PlaylistViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val caption: TextView = itemView.findViewById(R.id.titlePlaylist)
    private val trackCount: TextView = itemView.findViewById(R.id.trackCount)
    private val cover: ImageView = itemView.findViewById(R.id.coverPlaylist)


    fun bind(playlist: PlaylistCard) {

        caption.text = playlist.caption

        trackCount.text =
            itemView.context.resources.getQuantityString(
                R.plurals.tracks_plurals,
                playlist.countTrack,
                playlist.countTrack
            )


        Glide.with(itemView)
            .load(playlist.coverImg)
            .placeholder(R.drawable.placeholder_ico)
            .transform(FitCenter(), RoundedCorners(TrackHolder.dpToPx(8f)))
            .into(cover)

        caption.requestLayout()

    }
}
