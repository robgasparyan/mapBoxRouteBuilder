package com.mapbox.mapboxroutebuilder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.mapboxroutebuilder.utils.MapBoxHelper
import com.mapbox.mapboxroutebuilder.viewModels.BoxViewModel
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
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private var mapView: MapView? = null
    private val boxViewModel: BoxViewModel by viewModel()
    private val mapBoxHelper: MapBoxHelper by inject()
    private var mapBoxMap: MapboxMap? = null
    private var bottomSheet: FrameLayout? = null
    private var bottomShitBehaviour: BottomSheetBehavior<FrameLayout>? = null
    private var navigationMapRoute: NavigationMapRoute? = null
    private var carInfoTextView: AppCompatTextView? = null
    private var plateNumberTextView: AppCompatTextView? = null
    private var fuelPercentageTextView: AppCompatTextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        boxViewModel.getCarsList {
            mapView?.getMapAsync(this@MainActivity)
        }
        initViewComponents()
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

            val list = mapBoxHelper.getLatLngFromCarsModel(boxViewModel.carsList)
            if (list.size == 2) {
                setup(Pair("blue", list[0]), Pair("black", list[1]))
            }
        }
        this.mapBoxMap = mapboxMap


    }

    private fun setup(vararg iconIdLatLng: Pair<String, LatLng>) {
        val symbolManager = mapView?.let {
            mapBoxMap?.let { it1 ->
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

        iconIdLatLng.forEach {
            symbolManager?.create(
                SymbolOptions()
                    .withLatLng(it.second)
                    .withIconImage(it.first)
                    .withIconSize(1.0f)
            )
        }


        symbolManager?.addClickListener {
            when (it.iconImage) {
                "blue" -> {
                    if (mapBoxMap != null) {
                        val originPoint: Point = Point.fromLngLat(
                            mapBoxMap!!.locationComponent.lastKnownLocation!!.longitude,
                            mapBoxMap!!.locationComponent.lastKnownLocation!!.latitude
                        )
                        val destinationPoint: Point = Point.fromLngLat(
                            iconIdLatLng[0].second.longitude,
                            iconIdLatLng[0].second.latitude
                        )

                        boxViewModel.getDirections(
                            originPoint,
                            destinationPoint
                        ) { directionsRoute ->
                            if (navigationMapRoute != null) {
                                navigationMapRoute?.updateRouteVisibilityTo(true)
                            } else {

                                mapView?.let { view ->
                                    mapBoxMap?.let { box ->
                                        navigationMapRoute = NavigationMapRoute(
                                            null,
                                            view,
                                            box,
                                            R.style.NavigationMapRoute
                                        )
                                    }
                                }
                            }

                            navigationMapRoute?.addRoute(directionsRoute)
                        }
                    }

                    showCarInfo(0)


                }
                "black" -> {
                    if (mapBoxMap != null) {
                        val originPoint: Point = Point.fromLngLat(
                            mapBoxMap!!.locationComponent.lastKnownLocation!!.longitude,
                            mapBoxMap!!.locationComponent.lastKnownLocation!!.latitude
                        )
                        val destinationPoint: Point = Point.fromLngLat(
                            iconIdLatLng[1].second.longitude,
                            iconIdLatLng[1].second.latitude
                        )
                        boxViewModel.getDirections(
                            originPoint,
                            destinationPoint
                        ) { directionsRoute ->
                            if (navigationMapRoute != null) {
                                navigationMapRoute?.updateRouteVisibilityTo(false)
                            } else {

                                mapView?.let { view ->
                                    mapBoxMap?.let { box ->
                                        NavigationMapRoute(
                                            null,
                                            view,
                                            box,
                                            R.style.NavigationMapRoute
                                        )
                                    }
                                }
                            }

                            navigationMapRoute?.addRoute(directionsRoute)
                        }

                    }
                    showCarInfo(1)
                }
            }
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


    private fun enableLocationComponent() {

        val locationComponentOptions = LocationComponentOptions.builder(this)
            .pulseEnabled(true)
            .pulseColor(Color.GREEN)
            .pulseAlpha(.4f)
            .pulseInterpolator(BounceInterpolator())
            .build()

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            val locationComponent = mapBoxMap?.locationComponent

            mapBoxMap?.getStyle {
                locationComponent?.activateLocationComponent(
                    LocationComponentActivationOptions
                        .builder(this, it)
                        .locationComponentOptions(locationComponentOptions)
                        .build()
                )
            }
            // Activate with a built LocationComponentActivationOptions object


            // Enable to make component visible
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
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

    private fun initViewComponents() {
        bottomShitBehaviour = bottomSheet?.let { BottomSheetBehavior.from(it) }
        carInfoTextView = findViewById(R.id.carInfo)
        plateNumberTextView = findViewById(R.id.plateNumber)
        fuelPercentageTextView = findViewById(R.id.fuelPercentage)
        bottomSheet = findViewById(R.id.bottomSheet)
    }

    @SuppressLint("SetTextI18n")
    private fun showCarInfo(carId: Int) {
        carInfoTextView?.text = boxViewModel.carsList[carId].name
        plateNumberTextView?.text = boxViewModel.carsList[carId].plateNumber
        fuelPercentageTextView?.text = "${boxViewModel.carsList[carId].fuelPercentage}%"
        bottomShitBehaviour?.state = BottomSheetBehavior.STATE_EXPANDED
    }

}