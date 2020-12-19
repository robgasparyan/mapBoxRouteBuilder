package com.mapbox.mapboxroutebuilder.repositories

import androidx.lifecycle.liveData
import com.mapbox.mapboxroutebuilder.services.BoxApiService
import com.mapbox.mapboxroutebuilder.utils.getRandomItemFromList
import kotlinx.coroutines.Dispatchers

class BoxRepository(private val boxApiService: BoxApiService) {

    fun getCarsList() = liveData(Dispatchers.IO) {
        try {
            val result = boxApiService.getCarsList()
            val twoRandomCarList =
                listOf(result.getRandomItemFromList(), result.getRandomItemFromList())
            emit(Result.success(twoRandomCarList))
        } catch (e: Exception) {
//            emit(Result.failure(e))
        }
    }

}