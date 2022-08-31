package com.yuriisurzhykov.pointdetector.presentation.core

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

abstract class AbstractPermissionCheckActivity : AbstractNavigationActivity() {

    abstract fun getPermissionsArray(): Array<String>

    open fun onPermissionsGranted() {}

    open fun onPermissionsDenied(neverAskAgain: Boolean) {}

    open fun onShowPermissionsRationale() {}

    protected fun checkPermissions(): Boolean {
        return getPermissionsArray().all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
    }

    protected fun requestPermissions() {
        requestPermissions(getPermissionsArray(), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onPermissionsGranted()
            } else {
                onPermissionsDenied(checkNeverAskAgain())
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected fun checkNeverAskAgain(): Boolean {
        getPermissionsArray().forEach {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, it)) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

}