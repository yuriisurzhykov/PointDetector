package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractStyleFragment

abstract class AbstractPermissionFragment : AbstractStyleFragment, PermissionsResource {

    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onRequestPermissionsResult(it)
        }

    private val settingsLauncher =
        registerForActivityResult(PermissionsCheckActivity.SettingsActivityContract { requireContext() }) {
            if (it) {
                onPermissionsGranted()
            }
        }

    override fun onPermissionsGranted() {}

    open fun onPermissionsDenied(shouldShowRationale: Boolean) {}

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

    override fun requestPermissions() {
        permissionsLauncher.launch(getPermissionsArray())
    }

    protected fun openSettingsScreen() {
        settingsLauncher.launch(getPermissionsArray())
    }

    private fun onRequestPermissionsResult(
        grantResults: Map<String, Boolean>
    ) {
        if (grantResults.isNotEmpty() && grantResults.all { it.value }) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied(checkShowRationale())
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