package com.example.itunesapp2

import android.app.Application
import com.example.itunesapp2.di.AppModule
import com.example.itunesapp2.di.DI
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.ktp.KTP

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initToothpick()
        initLogger()
        initAppScope()
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction())
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initAppScope() {
        KTP.openScope(DI.APP_SCOPE).installModules(AppModule())
    }
}