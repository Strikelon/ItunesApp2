package com.example.itunesapp2.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapp2.R
import com.example.itunesapp2.model.entity.ResultTrackResponse
import kotlinx.android.synthetic.main.track_item.view.*
import timber.log.Timber

class TrackRecyclerViewAdapter(private var trackList: List<ResultTrackResponse.TrackResponse> = listOf()) : RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackViewHolder>() {

    companion object {
        private const val NO_INFO = "No info"
    }

    class TrackViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItem(track: ResultTrackResponse.TrackResponse) {
            val trackDetailText = "${track.trackName ?: NO_INFO} - ${track.trackPrice ?: NO_INFO} ${track.currency ?: NO_INFO}"
            itemView.track_detail.text = trackDetailText
        }
    }

    override fun getItemCount() = trackList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bindItem(trackList[position])
    }

    fun setData(data: List<ResultTrackResponse.TrackResponse>) {
        trackList = data
        notifyDataSetChanged()
    }

}