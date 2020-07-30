package com.example.itunesapp2.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapp2.R
import com.example.itunesapp2.di.DI
import com.example.itunesapp2.extensions.hideKeyboard
import com.example.itunesapp2.extensions.showSnackMessage
import com.example.itunesapp2.extensions.visible
import com.example.itunesapp2.utils.ResponseStatus
import com.example.itunesapp2.view.adapters.AlbumRecyclerViewAdapter
import com.example.itunesapp2.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import timber.log.Timber
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding

class MainFragment : BaseFragment() {

    val viewModel: MainViewModel by inject<MainViewModel>()

    companion object {
        private const val DEBOUNCE_PERIOD = 800L
    }

    override val layoutRes = R.layout.fragment_main

    private val albumAdapter: AlbumRecyclerViewAdapter = AlbumRecyclerViewAdapter()

    private var coroutineScope = this@MainFragment.lifecycle.coroutineScope
    private var searchJob: Job? = null
    private var oldSearchQuery: String = ""

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
        setupUI()
        setupRecyclerView()
        observeAlbumsServerResponse()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(context, 2)
        album_recycler_view.layoutManager = layoutManager
        album_recycler_view.adapter = albumAdapter
        albumAdapter.setListener {
            activity?.hideKeyboard()
            viewModel.onAlbumClick(it)
        }
        album_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 1) {
                    activity?.hideKeyboard()
                }
            }
        })
    }

    private fun setupUI() {
        main_edit_text.addTextChangedListener( object : TextWatcher {

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    query?.let {
                        delay(DEBOUNCE_PERIOD)
                        val searchQuery = query.toString()
                        when {
                            searchQuery.trim().isEmpty() -> {
                                viewModel.resetSearch()
                            }
                            oldSearchQuery == searchQuery -> { }
                            else -> {
                                oldSearchQuery = searchQuery
                                viewModel.getAlbums(oldSearchQuery)
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(editable: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })
    }

    private fun observeAlbumsServerResponse() {
        viewModel.albumServerResponse().observe(viewLifecycleOwner, Observer { serverResponse ->
            when (serverResponse.status) {
                ResponseStatus.LOADING -> {
                    Timber.tag("MYTAG").i("ResponseStatus.LOADING")
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