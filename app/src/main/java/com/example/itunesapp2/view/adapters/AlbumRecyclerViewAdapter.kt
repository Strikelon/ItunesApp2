package com.example.itunesapp2.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapp2.R
import com.example.itunesapp2.extensions.loadImage
import com.example.itunesapp2.model.entity.ResultCollectionResponse
import kotlinx.android.synthetic.main.album_item.view.*

class AlbumRecyclerViewAdapter(private var albumList: List<ResultCollectionResponse.CollectionResponse> = listOf()) : RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumViewHolder>() {

    private var listener: ((ResultCollectionResponse.CollectionResponse) -> (Unit) )? = null

    inner class AlbumViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItem(album: ResultCollectionResponse.CollectionResponse) {
            itemView.album_image.loadImage(album.artworkUrl100)
            itemView.album_name.text = album.collectionName
            itemView.setOnClickListener {
                listener?.invoke(album)
            }
        }
    }

    override fun getItemCount() = albumList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.album_item, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindItem(albumList[position])
    }

    fun setData(data: List<ResultCollectionResponse.CollectionResponse>) {
        albumList = data
        notifyDataSetChanged()
    }

    fun setListener(listen: (ResultCollectionResponse.CollectionResponse) -> (Unit)) {
        listener = listen
    }

}