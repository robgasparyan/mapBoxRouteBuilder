package com.mapbox.mapboxroutebuilder.Services

import com.mapbox.mapboxroutebuilder.Models.CarsModel
import retrofit2.http.GET
import retrofit2.http.Url

interface BoxApiService {

    @GET
    suspend fun getCarsList(@Url carsUrl: String = "https://raw.githubusercontent.com/Gary111/TrashCan/master/2000_cars.json"): CarsModel

}