package com.example.itunesapp2.view

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapp2.R
import com.example.itunesapp2.di.DI
import com.example.itunesapp2.extensions.hideKeyboard
import com.example.itunesapp2.extensions.showSnackMessage
import com.example.itunesapp2.extensions.visible
import com.example.itunesapp2.model.entity.ResultCollectionResponse
import com.example.itunesapp2.model.repository.ServerRepository
import com.example.itunesapp2.utils.ResponseStatus
import com.example.itunesapp2.utils.ServerResponse
import com.example.itunesapp2.view.adapters.AlbumRecyclerViewAdapter
import com.example.itunesapp2.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding

class MainFragment : BaseFragment() {

    val viewModel: MainViewModel by inject<MainViewModel>()

    override val layoutRes = R.layout.fragment_main

    private val albumAdapter: AlbumRecyclerViewAdapter = AlbumRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("MYTAG").i("fragment onCreate")
        injectDependencies()
    }

    private fun injectDependencies() {
        KTP.openScope(DI.APP_SCOPE)
            .openSubScope(DI.MAIN_SCREEN_SCOPE) { scope ->
                scope.installViewModelBinding<MainViewModel>(this)
                    .closeOnViewModelCleared(this)
            }
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("MYTAG").i("fragment onViewCreated")
        setupUI()
        setupRecyclerView()
        observeAlbumsServerResponse()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(context, 2)
        album_recycler_view.layoutManager = layoutManager
        album_recycler_view.adapter = albumAdapter
        albumAdapter.setListener {
            viewModel.onAlbumClick(it)
        }
    }

    private fun setupUI() {
        main_search_button.setOnClickListener {
            val query = main_edit_text.text.toString().trim()
            activity?.hideKeyboard()
            if(TextUtils.isEmpty(query)) {
                showSnackMessage(getString(R.string.query_empty))
            } else {
                main_edit_text.setText("")
                viewModel.getAlbums(query)
            }
        }
    }

    private fun observeAlbumsServerResponse() {
        viewModel.albumServerResponse().observe(viewLifecycleOwner, Observer { serverResponse ->
            when (serverResponse.status) {
                ResponseStatus.LOADING -> {
                    progress_bar.visible(true)
                }
                ResponseStatus.CANCELLED -> {
                    progress_bar.visible(false)
                }
                ResponseStatus.ERROR -> {
                    progress_bar.visible(false)
                    showSnackMessage(getString(R.string.network_error))
                }
                ResponseStatus.SUCCESS -> {
                    Timber.tag("MYTAG").i("ResponseStatus.SUCCESS")
                    progress_bar.visible(false)
                    serverResponse.data?.let { data ->
                        albumAdapter.setData(data)
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}