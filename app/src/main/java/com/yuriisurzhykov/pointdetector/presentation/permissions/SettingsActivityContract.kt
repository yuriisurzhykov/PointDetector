package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat

/**
 * Opens the settings of the app, where the permissions can be switched on, and answers whether
 * all of the asked permissions are granted when the person comes back.
 */
class SettingsActivityContract(private val contextProvider: () -> Context) :
    ActivityResultContract<Array<String>, Boolean>() {

    private var asked: Array<String> = emptyArray()

    override fun createIntent(context: Context, input: Array<String>): Intent {
        asked = input
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean = asked.isNotEmpty() && asked.all {
        ContextCompat.checkSelfPermission(contextProvider.invoke(), it) == PackageManager.PERMISSION_GRANTED
    }
}
