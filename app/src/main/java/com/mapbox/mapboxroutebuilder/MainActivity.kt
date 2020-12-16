package com.mapbox.mapboxroutebuilder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mapbox.mapboxroutebuilder.ViewModels.BoxViewModel
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

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
        mapboxMap.setStyle(Style.Builder().fromUri(getString(R.string.mapbox_style)).withImage("blue",
                ContextCompat.getDrawable(this, R.drawable.car_silhouette_blue)!!)
                .withImage("black", ContextCompat.getDrawable(this, R.drawable.car_silhouette_black)!!)) {
            setup()
        }
        this.mapboxMap = mapboxMap

    }

    private fun setup() {
        val symbolManager = mapView?.let { mapboxMap?.let { it1 -> it1.style?.let { it2 -> SymbolManager(it, it1, it2) } } }

        symbolManager?.iconAllowOverlap = true
        symbolManager?.iconIgnorePlacement = true


        symbolManager?.create(SymbolOptions()
                .withLatLng(LatLng(55.751244, 37.618423))
                .withIconImage("1")
                .withTextJustify("AAA")
                .withTextField("Rob")
                .withIconSize(2.0f))

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
}