package com.example.itunesapp2.viewmodel

import androidx.lifecycle.*
import com.example.itunesapp2.model.entity.ResultCollectionResponse
import com.example.itunesapp2.model.repository.ServerRepository
import com.example.itunesapp2.navigation.Screens
import com.example.itunesapp2.utils.ServerResponse
import kotlinx.coroutines.*
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

class MainViewModel: ViewModel() {

    private val router: Router by inject<Router>()
    private val serverRepository: ServerRepository by inject<ServerRepository>()

    private var albumLiveData: MutableLiveData<ServerResponse<List<ResultCollectionResponse.CollectionResponse>>> = MutableLiveData()

    private var job: Job? = null

    private val handler = CoroutineExceptionHandler { context, exception ->
        albumLiveData.value = (ServerResponse.error(exception.message ?: "Error Occurred!"))
    }

    fun albumServerResponse(): LiveData<ServerResponse<List<ResultCollectionResponse.CollectionResponse>>> = albumLiveData

    fun onAlbumClick(album: ResultCollectionResponse.CollectionResponse) {
        cancelJob()
        router.navigateTo(Screens.DetailScreen(album))
    }

    fun resetSearch() {
        cancelJob()
        albumLiveData.value = ServerResponse.success(data = listOf())
    }

    fun getAlbums(query: String) {
        cancelJob()
        job = viewModelScope.launch(handler) {
            albumLiveData.value = ServerResponse.loading()
            val collectionList = withContext(Dispatchers.IO) {
                serverRepository.downloadCollections(query)
            }
            albumLiveData.value = ServerResponse.success(data = collectionList)
        }
    }

    private fun cancelJob() {
        job?.let {
            if (it.isActive) {
                it.cancel()
                albumLiveData.value = ServerResponse.cancelled()
            }
        }
    }

    fun onBackPressed() {
        router.exit()
    }

}