package com.mapbox.mapboxroutebuilder.services.network

import com.mapbox.mapboxroutebuilder.models.CarsModel
import retrofit2.http.GET
import retrofit2.http.Url

interface BoxApiService {

    @GET
    suspend fun getCarsList(@Url carsUrl: String = "https://raw.githubusercontent.com/Gary111/TrashCan/master/2000_cars.json"): CarsModel

}