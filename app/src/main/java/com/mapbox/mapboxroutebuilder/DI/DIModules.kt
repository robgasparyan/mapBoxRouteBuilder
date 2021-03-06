package com.mapbox.mapboxroutebuilder.DI


import androidx.preference.PreferenceManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mapbox.android.core.BuildConfig
import com.mapbox.mapboxroutebuilder.repositories.BoxRepository
import com.mapbox.mapboxroutebuilder.services.network.BoxApiService
import com.mapbox.mapboxroutebuilder.services.network.CacheService
import com.mapbox.mapboxroutebuilder.utils.GsonUtils
import com.mapbox.mapboxroutebuilder.utils.MapBoxHelper
import com.mapbox.mapboxroutebuilder.viewModels.BoxViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.factory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appRepositories: Module = module {
    factory<BoxRepository>()
}

val viewModels: Module = module {
    viewModel<BoxViewModel>()
}

val helpers: Module = module {
    single { MapBoxHelper() }
    single { GsonUtils(get()) }
    single { CacheService(get()) }
    single { PreferenceManager.getDefaultSharedPreferences(get()) }
}

val networkModule: Module = module {

    // Gson
    single {
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setPrettyPrinting()
            .create()
    }

    // OkHttpClient
    single {
        val okHttpBuilder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        // Also you can add your own interceptor
        okHttpBuilder
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(
                10,
                TimeUnit.MINUTES
            ) // fix exception when uploading files with slow internet
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    // Retrofit
    single {
        val BASE_URL = "https://api.mapbox.com"
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .build()
    }
}

val services: Module = module {
    single { get<Retrofit>().create(BoxApiService::class.java) }
}