package com.mapbox.mapboxroutebuilder.Models


import com.google.gson.annotations.SerializedName

class CarsModel : ArrayList<CarsModel.CarsModelItem>() {
    data class CarsModelItem(
        @SerializedName("angle")
        val angle: Int,
        @SerializedName("color")
        val color: String,
        @SerializedName("fuel_percentage")
        val fuelPercentage: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("longitude")
        val longitude: Double,
        @SerializedName("name")
        val name: String,
        @SerializedName("plate_number")
        val plateNumber: String
    )
}