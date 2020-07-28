package com.example.itunesapp2.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapp2.R
import com.example.itunesapp2.di.DI
import com.example.itunesapp2.extensions.loadImage
import com.example.itunesapp2.extensions.showSnackMessage
import com.example.itunesapp2.extensions.visible
import com.example.itunesapp2.model.entity.ResultCollectionResponse
import com.example.itunesapp2.utils.ResponseStatus
import com.example.itunesapp2.view.adapters.TrackRecyclerViewAdapter
import com.example.itunesapp2.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class DetailFragment : BaseFragment() {

    val viewModel: DetailViewModel by inject<DetailViewModel>()

    override val layoutRes = R.layout.fragment_detail

    private val trackAdapter: TrackRecyclerViewAdapter = TrackRecyclerViewAdapter()

    companion object {
        private const val ALBUM_KEY = "ALBUM_KEY"

        fun newInstance(collectionResponse: ResultCollectionResponse.CollectionResponse) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ALBUM_KEY, collectionResponse)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    private fun injectDependencies() {
        KTP.openScope(DI.APP_SCOPE)
            .installViewModelBinding<DetailViewModel>(this)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        showAlbumInfo()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        track_recycler_view.layoutManager = layoutManager
        track_recycler_view.adapter = trackAdapter
    }

    private fun showAlbumInfo() {
        arguments?.let {
            val collection = it.getParcelable(ALBUM_KEY) as ResultCollectionResponse.CollectionResponse?
            collection?.let {
                showAlbumInfo(collection)
                viewModel.getSongs(collection).observe(viewLifecycleOwner, Observer { serverResponse ->
                    when (serverResponse.status) {
                        ResponseStatus.LOADING -> {
                            progress_bar.visible(true)
                        }
                        ResponseStatus.ERROR -> {
                            progress_bar.visible(false)
                            showSnackMessage(getString(R.string.network_error))
                        }
                        ResponseStatus.SUCCESS -> {
                            progress_bar.visible(false)
                            serverResponse.data?.let { data ->
                                trackAdapter.setData(data)
                            }
                        }
                    }
                })
            }
        }
    }

    private fun showAlbumInfo(collection: ResultCollectionResponse.CollectionResponse) {
        album_name.text = collection.collectionName
        artist_name.text = collection.artistName
        val collectionYear = "${collection.primaryGenreName ?: ""} - ${collection.getCollectionYear()}"
        genre_and_release_year.text = collectionYear
        val albumDetail = "${collection.trackCount} ${getString(R.string.songs)} - ${collection.collectionPrice} ${collection.currency}"
        album_detail.text = albumDetail
        album_photo.loadImage(collection.artworkUrl100)
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}