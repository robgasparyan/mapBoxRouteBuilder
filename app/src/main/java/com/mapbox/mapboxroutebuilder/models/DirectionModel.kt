package com.mapbox.mapboxroutebuilder.models


import com.google.gson.annotations.SerializedName

data class DirectionModel(
    @SerializedName("code")
    val code: String,
    @SerializedName("routes")
    val routes: List<Route>,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("waypoints")
    val waypoints: List<Waypoint>
) {
    data class Route(
        @SerializedName("distance")
        val distance: Double,
        @SerializedName("duration")
        val duration: Double,
        @SerializedName("geometry")
        val geometry: Geometry,
        @SerializedName("legs")
        val legs: List<Leg>,
        @SerializedName("weight")
        val weight: Double,
        @SerializedName("weight_name")
        val weightName: String
    ) {
        data class Geometry(
            @SerializedName("coordinates")
            val coordinates: List<List<Double>>,
            @SerializedName("type")
            val type: String
        )

        data class Leg(
            @SerializedName("admins")
            val admins: List<Admin>,
            @SerializedName("distance")
            val distance: Double,
            @SerializedName("duration")
            val duration: Double,
            @SerializedName("steps")
            val steps: List<Any>,
            @SerializedName("summary")
            val summary: String,
            @SerializedName("weight")
            val weight: Double
        ) {
            data class Admin(
                @SerializedName("iso_3166_1")
                val iso31661: String,
                @SerializedName("iso_3166_1_alpha3")
                val iso31661Alpha3: String
            )
        }
    }

    data class Waypoint(
        @SerializedName("distance")
        val distance: Double,
        @SerializedName("location")
        val location: List<Double>,
        @SerializedName("name")
        val name: String
    )
}