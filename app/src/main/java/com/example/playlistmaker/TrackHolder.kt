package com.example.playlistmaker

import android.content.res.Resources.getSystem
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val duration: TextView = itemView.findViewById(R.id.durationTextView)
    private val cover: ImageView = itemView.findViewById(R.id.coverAlbum)

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            getSystem().displayMetrics
        ).toInt()
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        duration.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_ico)
            .transform(RoundedCorners(dpToPx(2f)))
            .centerCrop()
            .into(cover)
    }

}