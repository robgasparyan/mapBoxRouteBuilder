package com.mapbox.mapboxroutebuilder.utils

import com.mapbox.mapboxroutebuilder.models.CarsModel
import com.mapbox.mapboxsdk.geometry.LatLng

class MapBoxHelper {
    fun getLatLngFromCarsModel(list: List<CarsModel.CarsModelItem>): List<LatLng> {
        val returnList = arrayListOf<LatLng>()
        list.map {
            returnList.add(LatLng(it.latitude, it.longitude))
        }
        return returnList
    }
}