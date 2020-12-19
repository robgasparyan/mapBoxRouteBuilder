package com.mapbox.mapboxroutebuilder

import android.graphics.Color
import android.os.Bundle
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxroutebuilder.ViewModels.BoxViewModel
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private var mapView: MapView? = null
    private val boxViewModel: BoxViewModel by viewModel()
    private var mapboxMap: MapboxMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.cameraPosition =
            CameraPosition.Builder()
                .target(LatLng(55.751244, 37.618423))
                .zoom(8.0)
                .tilt(20.0)
                .build()
        mapboxMap.setStyle(
            Style.Builder().fromUri(getString(R.string.mapbox_style)).withImage(
                "blue",
                ContextCompat.getDrawable(this, R.drawable.car_silhouette_blue)!!
            )
                .withImage(
                    "black",
                    ContextCompat.getDrawable(this, R.drawable.car_silhouette_black)!!
                )
        ) {

            enableLocationComponent()



            setup("blue", LatLng(65.3, 37.9))
            setup("black", LatLng(55.3, 38.9))
        }
        this.mapboxMap = mapboxMap


    }

    private fun setup(iconId: String, latlng: LatLng) {
        val symbolManager = mapView?.let {
            mapboxMap?.let { it1 ->
                it1.style?.let { it2 ->
                    SymbolManager(
                        it,
                        it1,
                        it2
                    )
                }
            }
        }

        symbolManager?.iconAllowOverlap = true
        symbolManager?.iconIgnorePlacement = true


        symbolManager?.create(
            SymbolOptions()
                .withLatLng(latlng)
                .withIconImage(iconId)
                .withTextJustify("AAA")
                .withTextField("Rob")
                .withIconSize(1.0f)
        )

//        symbolManager?.create(SymbolOptions()
//                .withLatLng(LatLng(55.751211, 37.618423))
//                .withIconImage("2")
//                .withIconSize(2.0f))
        symbolManager?.addClickListener {

            return@addClickListener true
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    @SuppressWarnings("MissingPermission")
    private fun enableLocationComponent() {

        val locationComponentOptions = LocationComponentOptions.builder(this)
            .pulseEnabled(true)
            .pulseColor(Color.GREEN)
            .pulseAlpha(.4f)
            .pulseInterpolator(BounceInterpolator())
            .build();

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            val locationComponent = mapboxMap?.locationComponent

            mapboxMap?.getStyle {
                locationComponent?.activateLocationComponent(
                    LocationComponentActivationOptions
                        .builder(this, it)
                        .locationComponentOptions(locationComponentOptions)
                        .build()
                )
            }
            // Activate with a built LocationComponentActivationOptions object


            // Enable to make component visible
            locationComponent?.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent?.cameraMode = CameraMode.TRACKING

            // Set the component's render mode
            locationComponent?.renderMode = RenderMode.COMPASS

        } else {

            val permissionsManager = PermissionsManager(this)

            permissionsManager.requestLocationPermissions(this)

        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
    }


    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionsManager(this).onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}