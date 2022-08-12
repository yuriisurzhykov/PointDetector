package com.yuriisurzhykov.pointdetector.presentation.core

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class AbstractPermissionCheckActivity : AppCompatActivity() {

    abstract fun getPermissionsArray(): Array<String>

    open fun onPermissionsGranted() {}

    open fun onPermissionsDenied() {}

    open fun onShowPermissionsRationale() {}

    protected fun checkPermissions() {
        getPermissionsArray().all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
    }

    protected fun requestPermissions() {
        ActivityCompat.requestPermissions(this, getPermissionsArray(), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onPermissionsGranted()
            } else {
                if (checkShowRationale()) {
                    onShowPermissionsRationale()
                } else {
                    onPermissionsDenied()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkShowRationale(): Boolean {
        getPermissionsArray().forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, it)) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

}