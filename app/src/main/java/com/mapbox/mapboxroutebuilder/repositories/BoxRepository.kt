package com.mapbox.mapboxroutebuilder.repositories

import androidx.lifecycle.liveData
import com.mapbox.mapboxroutebuilder.Utils.getRandomItemFromList
import com.mapbox.mapboxroutebuilder.services.network.BoxApiService
import com.mapbox.mapboxroutebuilder.services.network.CacheService
import kotlinx.coroutines.Dispatchers


class BoxRepository(
    private val boxApiService: BoxApiService,
    private val cacheLayer: CacheService
) {

    fun getCarsList() = liveData(Dispatchers.IO) {
        try {
            if (cacheLayer.getCacheData().isNotEmpty()) {
                emit(Result.success(cacheLayer.getCacheData()))
                return@liveData
            }
            val result = boxApiService.getCarsList()
            val twoRandomCarList =
                listOf(result.getRandomItemFromList(), result.getRandomItemFromList())
            cacheLayer.cacheData(twoRandomCarList)
            emit(Result.success(twoRandomCarList))
        } catch (e: Exception) {
//            emit(Result.failure(e))
        }
    }

}