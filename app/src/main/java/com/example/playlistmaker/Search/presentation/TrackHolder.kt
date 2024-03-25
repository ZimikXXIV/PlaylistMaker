package com.example.playlistmaker.Search.presentation

import android.content.Intent
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Debounce.clickDebounce
import com.example.playlistmaker.Player.domain.PlayerConst
import com.example.playlistmaker.Player.presentation.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.data.repository.TrackListClickListenerInterface
import com.example.playlistmaker.Search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale


class TrackHolder(itemView: View, private val onClick: TrackListClickListenerInterface?) :
    RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val duration: TextView = itemView.findViewById(R.id.durationTextView)
    private val cover: ImageView = itemView.findViewById(R.id.coverAlbum)



    fun bind(track: Track) {

        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis!!.toLong())

        itemView.setOnClickListener {
            onClick?.onClick(track)

            if (!clickDebounce()) {
                return@setOnClickListener
            }

            val intent = Intent(itemView.context, AudioPlayerActivity::class.java)
            intent.putExtra(PlayerConst.TRACK_INFO, track)
            it.context.startActivity(intent)
        }

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder_ico)
            .transform(RoundedCorners(dpToPx(2f)))
            .centerCrop()
            .into(cover)
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
