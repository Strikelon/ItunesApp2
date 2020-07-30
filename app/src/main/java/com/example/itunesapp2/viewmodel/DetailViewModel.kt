package com.example.itunesapp2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.itunesapp2.model.entity.ResultCollectionResponse
import com.example.itunesapp2.model.entity.ResultTrackResponse
import com.example.itunesapp2.model.repository.ServerRepository
import com.example.itunesapp2.utils.ServerResponse
import kotlinx.coroutines.Dispatchers
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

class DetailViewModel: ViewModel() {

    private val router: Router by inject<Router>()
    private val serverRepository: ServerRepository by inject<ServerRepository>()

    fun getSongs(collection: ResultCollectionResponse.CollectionResponse) : LiveData<ServerResponse<List<ResultTrackResponse.TrackResponse>>> {
        return liveData<ServerResponse<List<ResultTrackResponse.TrackResponse>>>(Dispatchers.IO) {
            emit(ServerResponse.loading())
            try {
                val songs = serverRepository.downloadTracks(collection.collectionId)
                emit(ServerResponse.success(data = songs))
            } catch (exception: Exception) {
                emit(ServerResponse.error(exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun onBackPressed() {
        router.exit()
    }

}