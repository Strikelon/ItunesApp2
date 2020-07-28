package com.example.itunesapp2.di.provider

import android.content.Context
import com.example.itunesapp2.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientProvider @Inject constructor(
) : Provider<OkHttpClient> {

    override fun get() = with(OkHttpClient.Builder()) {
        retryOnConnectionFailure(true)
        if (BuildConfig.DEBUG) {
            addNetworkInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
        }
        build()
    }
}