package com.mapbox.mapboxroutebuilder.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.mapboxroutebuilder.models.CarsModel

class GsonUtils(private val gson: Gson) {

    fun parseJsonToCarsModel(json: String): List<CarsModel.CarsModelItem> {
        val listType = object : TypeToken<List<CarsModel.CarsModelItem>>() {}.type
        return gson.fromJson(json, listType)
    }

}