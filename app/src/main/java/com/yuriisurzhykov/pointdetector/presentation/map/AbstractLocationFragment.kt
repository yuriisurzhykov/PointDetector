package com.yuriisurzhykov.pointdetector.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.yuriisurzhykov.pointdetector.presentation.permissions.AbstractPermissionFragment
import com.yuriisurzhykov.pointdetector.presentation.permissions.LocationPermissionResourceManager
import com.yuriisurzhykov.pointdetector.presentation.permissions.PermissionDenialFragment
import java.util.concurrent.Executors
import com.yuriisurzhykov.pointdetector.R

abstract class AbstractLocationFragment : AbstractPermissionFragment {

    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    private val locationService by lazy {
        LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
    }
    private val locationExecutor by lazy { Executors.newSingleThreadExecutor() }
    private val locationRequest by lazy {
        LocationRequest.Builder(5)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(0)
            .setMaxUpdateDelayMillis(3000)
            .build()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0.lastLocation?.let { onLocationReceived(it) }
        }
    }

    protected open fun onLocationReceived(location: Location) {

    }

    override fun getPermissionsArray(): Array<String> {
        return arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationService
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    override fun onStop() {
        super.onStop()
        releaseLocationCallback()
    }

    @SuppressLint("MissingPermission")
    override fun onPermissionsGranted() {
        startListenLocationUpdates()
        sendLastLocation()
    }

    override fun onPermissionsDenied(shouldShowRationale: Boolean) {
        if (shouldShowRationale) {
            openFragment(
                PermissionDenialFragment.newInstance(LocationPermissionResourceManager(this)),
                "denial_permission"
            )
        } else {
            view?.let { showPermissionsDeniedSnackbar(it) }
        }
    }

    private fun showPermissionsDeniedSnackbar(view: View) {
        Snackbar
            .make(
                view,
                getString(R.string.snackbar_location_permissions_denied),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.button_provide_location_permissions_never_ask) {
                openSettingsScreen()
            }.show()
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun startListenLocationUpdates() {
        locationService.requestLocationUpdates(locationRequest, locationExecutor, locationCallback)
    }

    private fun releaseLocationCallback() {
        locationService.removeLocationUpdates(locationCallback)
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun sendLastLocation() {
        locationService.lastLocation.addOnCompleteListener {
            if (it.result != null) onLocationReceived(
                it.result
            )
        }
    }

}