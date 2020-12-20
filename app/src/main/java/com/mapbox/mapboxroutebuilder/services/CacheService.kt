package com.mapbox.mapboxroutebuilder.services.network

import com.google.gson.Gson
import com.mapbox.mapboxroutebuilder.models.CarsModel
import com.sfl.rates.services.BoxPreference

class CacheService(private val gson: Gson) {

    fun cacheData(items: List<CarsModel.CarsModelItem>) {
        BoxPreference.putCacheData.set(gson.toJson(items))
    }

    // this method can return empty list, it means cache suicidal time is expired
    fun getCacheData(): List<CarsModel.CarsModelItem> {
        return emptyList()
    }
}