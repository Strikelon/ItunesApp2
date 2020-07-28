package com.example.itunesapp2.model.data.server

import com.example.itunesapp2.model.entity.ResultCollectionResponse
import com.example.itunesapp2.model.entity.ResultTrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("search")
    suspend fun downloadCollections (
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "album"
    ): ResultCollectionResponse

    @GET("lookup")
    suspend fun downloadTracks (
        @Query("id") id: String,
        @Query("entity") entity: String = "song"
    ): ResultTrackResponse

}