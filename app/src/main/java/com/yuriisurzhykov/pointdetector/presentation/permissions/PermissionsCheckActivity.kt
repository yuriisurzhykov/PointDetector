package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * The first screen of the app. With the location granted it goes straight to the main screen;
 * otherwise it explains why the location is needed and asks for it, or, when the system will
 * not ask again, leads to the settings. The person can always go on without the location.
 */
@AndroidEntryPoint
class PermissionsCheckActivity : ComponentActivity() {

    private var step by mutableStateOf(PermissionStep.Request)

    private val requestLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        if (result.isNotEmpty() && result.all { it.value }) {
            startMainScreen()
        } else {
            step = PermissionStep.afterDenial(shouldShowRationale())
        }
    }

    private val settingsLauncher = registerForActivityResult(SettingsActivityContract { this }) {
        if (it || isGranted()) startMainScreen()
    }

    private val actions = object : PermissionActions {
        override fun onGrant() = requestLauncher.launch(LOCATION_PERMISSIONS)
        override fun onOpenSettings() = settingsLauncher.launch(LOCATION_PERMISSIONS)
        override fun onContinue() = startMainScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isGranted()) {
            startMainScreen()
            return
        }
        enableEdgeToEdge()
        setContent {
            PdTheme { PermissionScreen(step, actions) }
        }
    }

    private fun isGranted(): Boolean = LOCATION_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRationale(): Boolean = LOCATION_PERMISSIONS.any {
        ActivityCompat.shouldShowRequestPermissionRationale(this, it)
    }

    private fun startMainScreen() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainActivityIntent)
        finish()
    }

    private companion object {
        val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
