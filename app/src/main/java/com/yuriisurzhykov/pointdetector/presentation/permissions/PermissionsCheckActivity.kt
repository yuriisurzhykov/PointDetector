package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat.createManageUnusedAppRestrictionsIntent
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractPermissionCheckActivity
import com.yuriisurzhykov.pointdetector.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionsCheckActivity : AbstractPermissionCheckActivity() {

    private var continueButton: Button? = null
    private var requestPermissionsButton: Button? = null

    private val settingsLauncher = registerForActivityResult(SettingsActivityContract { this }) {
        if (it) {
            startMainScreen()
        }
    }

    override fun getPermissionsArray(): Array<String> = LOCATION_PERMISSIONS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermissions()) {
            startMainScreen()
            return
        }
        setContentView(R.layout.activity_permissions_request)
        findViewById<ImageView>(R.id.image).setImageResource(R.drawable.ic_location)
        findViewById<TextView>(R.id.title).setText(R.string.title_location_permission_request)
        findViewById<TextView>(R.id.rationale).setText(R.string.rationale_location_permission_text)
        with(findViewById<Button>(R.id.retry)) {
            requestPermissionsButton = this
            setText(R.string.button_location_permissions_retry)
            setOnClickListener { if (checkNeverAskAgain()) openLocationSettings() else requestPermissions() }
        }
        with(findViewById<Button>(R.id.continue_button)) {
            setText(R.string.button_continue_anyway)
            continueButton = this
            setOnClickListener { startMainScreen() }
        }
    }

    override fun onPermissionsGranted() {
        super.onPermissionsGranted()
        startMainScreen()
    }

    override fun onPermissionsDenied(neverAskAgain: Boolean) {
        super.onPermissionsDenied(neverAskAgain)
        continueButton?.visibility = View.VISIBLE
        if (neverAskAgain) {
            requestPermissionsButton?.setText(R.string.button_provide_location_permissions_never_ask)
        }
    }

    override fun openMainFragment() {
    }

    override fun onStart() {
        super.onStart()
        requestPermissions()
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

    class SettingsActivityContract(private val contextProvider: () -> Context) :
        ActivityResultContract<Array<String>, Boolean>() {
        override fun createIntent(context: Context, input: Array<String>): Intent {
            return createManageUnusedAppRestrictionsIntent(context, context.packageName).apply {
                putExtra("permission_array", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            val permissionsArray = intent?.getStringArrayExtra("permissions_array")
            if (permissionsArray != null) {
                return permissionsArray.all {
                    ContextCompat.checkSelfPermission(contextProvider.invoke(), it) ==
                            PackageManager.PERMISSION_GRANTED
                }
            }
            return false
        }
    }

    companion object {
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}