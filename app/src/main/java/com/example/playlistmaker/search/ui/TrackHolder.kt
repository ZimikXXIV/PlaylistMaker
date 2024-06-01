package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.api.TrackListClickListenerInterface
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class TrackHolder(itemView: View, private val onClick: TrackListClickListenerInterface?) :
    RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val duration: TextView = itemView.findViewById(R.id.durationTextView)
    private val cover: ImageView = itemView.findViewById(R.id.coverAlbum)


    private var isClickAllowed = true

    fun bind(track: Track) {

        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis!!.toLong())

        itemView.setOnClickListener {
            onClick?.onClick(track)

            CoroutineScope(Dispatchers.IO).launch {

                if (isClickAllowed) {
                    isClickAllowed = false
                    delay(SearchConst.CLICK_DEBOUNCE_DELAY)
                    openPlayer(it.context, track)
                }

            }
        }

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder_ico)
            .transform(RoundedCorners(dpToPx(2f)))
            .centerCrop()
            .into(cover)

        artistName.requestLayout()

    }

    private fun openPlayer(context: Context, track: Track) {
        isClickAllowed = true
        val intent = Intent(itemView.context, AudioPlayerActivity::class.java)
        intent.putExtra(PlayerConst.TRACK_INFO, track)
        context.startActivity(intent)
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
