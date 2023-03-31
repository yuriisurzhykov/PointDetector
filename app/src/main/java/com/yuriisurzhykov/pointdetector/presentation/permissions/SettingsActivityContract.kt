package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat

class SettingsActivityContract(private val contextProvider: () -> Context) :
    ActivityResultContract<Array<String>, Boolean>() {
    override fun createIntent(context: Context, input: Array<String>): Intent {
        return IntentCompat.createManageUnusedAppRestrictionsIntent(context, context.packageName).apply {
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
        } else {

        }
        return false
    }
}