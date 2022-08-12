package com.yuriisurzhykov.pointdetector.presentation.core

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class AbstractPermissionFragment : Fragment {

    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onRequestPermissionsResult(it)
        }

    abstract fun getPermissionsArray(): Array<String>

    open fun onPermissionsGranted() {}

    open fun onPermissionsDenied() {}

    open fun onShowPermissionsRationale() {}

    protected fun checkPermissions() {
        context?.let { localContext ->
            val permissionGranted = getPermissionsArray().all {
                ContextCompat.checkSelfPermission(localContext, it) == PackageManager.PERMISSION_GRANTED
            }
            if (permissionGranted) {
                onPermissionsGranted()
            } else {
                requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        permissionsLauncher.launch(getPermissionsArray())
    }

    private fun onRequestPermissionsResult(
        grantResults: Map<String, Boolean>
    ) {
        if (grantResults.all { it.value }) {
            onPermissionsGranted()
        } else {
            if (checkShowRationale()) {
                onShowPermissionsRationale()
            } else {
                onPermissionsDenied()
            }
        }
    }

    private fun checkShowRationale(): Boolean {
        activity?.let { localActivity ->
            getPermissionsArray().forEach {
                if (ActivityCompat.shouldShowRequestPermissionRationale(localActivity, it)) {
                    return true
                }
            }
        }
        return false
    }

    companion object {

    }
}