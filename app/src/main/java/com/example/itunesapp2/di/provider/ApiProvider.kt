package com.example.itunesapp2.di.provider

import com.example.itunesapp2.BuildConfig
import com.example.itunesapp2.model.data.server.Api
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
    private val okHttpClient: OkHttpClient
): Provider<Api> {
    override fun get(): Api =
        Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(BuildConfig.API_BASE_URL)
        .build()
        .create(Api::class.java)
}