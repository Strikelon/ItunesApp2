package com.example.itunesapp2.model.repository

import com.example.itunesapp2.model.data.server.Api
import toothpick.InjectConstructor

@InjectConstructor
class ServerRepository (
    private val api: Api
) {

    suspend fun downloadCollections(term: String) =
        api.downloadCollections(term = term).results

    suspend fun downloadTracks(id: String) =
        api.downloadTracks(id = id).results

}