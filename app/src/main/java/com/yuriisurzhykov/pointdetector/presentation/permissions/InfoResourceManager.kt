package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View

interface InfoResourceManager {
    fun getDescriptionRationaleText(context: Context): String
    fun getInfoImage(context: Context): Drawable?
    fun getInfoTitle(context: Context): String
    fun getRetryButtonText(context: Context): String
    fun onButtonClick(activity: Activity, view: View)
}

interface PermissionsResource {
    fun getPermissionsArray(): Array<String>
    fun onPermissionsGranted()
    fun requestPermissions()
}

interface PermissionResourceManager : InfoResourceManager, PermissionsResource {
}