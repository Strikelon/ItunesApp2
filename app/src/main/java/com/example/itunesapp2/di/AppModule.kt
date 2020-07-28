package com.example.itunesapp2.di

import com.example.itunesapp2.di.provider.ApiProvider
import com.example.itunesapp2.di.provider.OkHttpClientProvider
import com.example.itunesapp2.model.data.server.Api
import com.example.itunesapp2.model.repository.ServerRepository
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule(): Module() {
    init {
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingleton()
        bind(Api::class.java).toProvider(ApiProvider::class.java).providesSingleton()
        bind(ServerRepository::class.java).singleton()
    }
}