package com.mapbox.mapboxroutebuilder.viewModels

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.mapbox.mapboxroutebuilder.models.CarsModel
import com.mapbox.mapboxroutebuilder.repositories.BoxRepository
import com.mapbox.mapboxroutebuilder.utils.observeOnce
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

class BoxViewModel(private val boxRepository: BoxRepository) : ViewModel() {

    var carsList = mutableListOf<CarsModel.CarsModelItem>()

    fun getCarsList(success: (() -> Unit)) {
        boxRepository.getCarsList().observeOnce { it ->
            it.getOrNull()?.let {
                carsList = it.toMutableList()
                success.invoke()
            }
        }
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