package com.mapbox.mapboxroutebuilder.viewModels

import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxroutebuilder.R
import com.mapbox.mapboxroutebuilder.Utils.observeOnce
import com.mapbox.mapboxroutebuilder.models.CarsModel
import com.mapbox.mapboxroutebuilder.repositories.BoxRepository
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoxViewModel(private val boxRepository: BoxRepository, private val context: Context) :
    ViewModel() {

    var carsList = mutableListOf<CarsModel.CarsModelItem>()

    fun getCarsList(success: (() -> Unit)) {
        boxRepository.getCarsList().observeOnce(Observer {
            it.getOrNull()?.let {
                carsList = it.toMutableList()
                success.invoke()
            }
        })
    }

    fun getDirections(origin: Point, destination: Point, callback: (DirectionsRoute) -> Unit) {
        NavigationRoute.builder(context)
            .accessToken(context.getString(R.string.mapbox_access_token))
            .origin(origin)
            .destination(destination)
            .build().getRoute(object : Callback<DirectionsResponse?> {
                override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<DirectionsResponse?>,
                    response: Response<DirectionsResponse?>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.routes()?.get(0)?.apply {
                            callback.invoke(this)
                        }
                    } else {
                        Log.e("TAG", "FAiled to draw route")
                    }
                }
            })


    }


    private fun changeCameraPosition(
        mapBox: MapboxMap,
        latitude: Double,
        longitude: Double,
        animate: Boolean = false,
        duration: Int = 5000,
        @DrawableRes marketIcon: Int = -1
    ) {

        val position: CameraPosition? = if (animate) {

            CameraPosition.Builder()
                .target(LatLng(latitude, longitude)) // Sets the new camera position
                .zoom(8.0) // Sets the zoom
                .bearing(90.toDouble()) // Rotate the camera
                .tilt(30.toDouble()) // Set the camera tilt
                .build() // Creates a CameraPosition from the builder

        } else {

            CameraPosition.Builder()
                .target(LatLng(latitude, longitude)) // Sets the new camera position
                .zoom(8.0) // Sets the zoom
                .build() // Creates a CameraPosition from the builder

        }

        position?.let {
            mapBox.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(it), duration
            )
        }
    }
}