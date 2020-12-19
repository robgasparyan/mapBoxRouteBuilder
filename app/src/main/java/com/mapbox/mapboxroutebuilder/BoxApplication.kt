package com.mapbox.mapboxroutebuilder

import android.app.Application
import com.mapbox.mapboxroutebuilder.DI.appRepositories
import com.mapbox.mapboxroutebuilder.DI.networkModule
import com.mapbox.mapboxroutebuilder.DI.services
import com.mapbox.mapboxroutebuilder.DI.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BoxApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BoxApplication)
            androidLogger(Level.NONE)
            modules(listOf(viewModels, appRepositories, services, networkModule))
        }
    }
}