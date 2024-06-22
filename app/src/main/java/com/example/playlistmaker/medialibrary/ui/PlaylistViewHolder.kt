package com.example.playlistmaker.medialibrary.ui

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        trackCount.text = "%s %s".format(
            playlist.countTrack.toString(),
            getStringCountTracks(playlist.countTrack)
        )

        Glide.with(itemView)
            .load(playlist.coverImg)
            .placeholder(R.drawable.placeholder_ico)
            .transform(RoundedCorners(TrackHolder.dpToPx(2f)))
            .centerCrop()
            .into(cover)

        caption.requestLayout()

    }

    fun getStringCountTracks(trackCount: Int): String {
        return when (trackCount) {
            1 -> "трек"
            2, 3, 4 -> "трека"
            else -> "треков"
        }
    }

    companion object {
        fun dpToPx(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                Resources.getSystem().displayMetrics
            ).toInt()
        }
    }
}
