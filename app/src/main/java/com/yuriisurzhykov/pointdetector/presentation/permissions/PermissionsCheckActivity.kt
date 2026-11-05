package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractPermissionCheckActivity
import com.yuriisurzhykov.pointdetector.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * The first screen of the app. With the location granted it goes straight to the main screen;
 * otherwise it explains why the location is needed and asks for it, or, when the system will
 * not ask again, leads to the settings. The person can always go on without the location.
 */
@AndroidEntryPoint
class PermissionsCheckActivity : AbstractPermissionCheckActivity() {

    private var step by mutableStateOf(PermissionStep.Request)

    private val settingsLauncher = registerForActivityResult(SettingsActivityContract { this }) {
        if (it || checkPermissions()) {
            startMainScreen()
        }
    }

    private val actions = object : PermissionActions {
        override fun onGrant() = requestPermissions()
        override fun onOpenSettings() = openLocationSettings()
        override fun onContinue() = startMainScreen()
    }

    override fun getPermissionsArray(): Array<String> = LOCATION_PERMISSIONS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermissions()) {
            startMainScreen()
            return
        }
        setContent {
            PdTheme { PermissionScreen(step, actions) }
        }
    }

    override fun onPermissionsGranted() {
        super.onPermissionsGranted()
        startMainScreen()
    }

    override fun onPermissionsDenied(shouldShowRationale: Boolean) {
        super.onPermissionsDenied(shouldShowRationale)
        step = PermissionStep.afterDenial(shouldShowRationale)
    }

    override fun openMainFragment() {
    }

    private fun startMainScreen() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainActivityIntent)
        finish()
    }

    private fun openLocationSettings() {
        settingsLauncher.launch(getPermissionsArray())
    }

    companion object {
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
