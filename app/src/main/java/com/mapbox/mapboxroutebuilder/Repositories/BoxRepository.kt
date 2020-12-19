package com.mapbox.mapboxroutebuilder.Repositories

import android.util.Log
import androidx.lifecycle.liveData
import com.mapbox.mapboxroutebuilder.Services.BoxApiService
import com.mapbox.mapboxroutebuilder.Utils.getRandomItemFromList
import kotlinx.coroutines.Dispatchers

class BoxRepository(private val boxApiService: BoxApiService) {

    fun getCarsList() = liveData(Dispatchers.IO) {
        Log.i("result", "result:InMethodRepo")
        try {
            val result = boxApiService.getCarsList()
            val twoRandomCarList =
                listOf(result.getRandomItemFromList(), result.getRandomItemFromList())
            Log.i("result", "result:$result")
            emit(Result.success(twoRandomCarList))
        } catch (e: Exception) {
            emit(Result.failure(e))
            Log.i("result", "result: failed")
        }
    }

}