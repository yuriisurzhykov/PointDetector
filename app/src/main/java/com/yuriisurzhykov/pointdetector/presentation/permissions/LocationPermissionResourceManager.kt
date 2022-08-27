package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.yuriisurzhykov.pointdetector.R
import java.lang.ref.SoftReference

class LocationPermissionResourceManager(parentFragment: AbstractPermissionFragment) :
    PermissionResourceManager {

    private val parentFragmentRef = SoftReference(parentFragment)

    override fun getDescriptionRationaleText(context: Context) =
        context.getString(R.string.rationale_location_permission_text)

    override fun getInfoImage(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.ic_no_gps_connection)
    }

    override fun getInfoTitle(context: Context) =
        context.getString(R.string.title_location_permission_denied)

    override fun getRetryButtonText(context: Context) =
        context.getString(R.string.button_location_permissions_retry)

    override fun onButtonClick(activity: Activity, view: View) {
        requestPermissions()
    }

    override fun requestPermissions() {
        parentFragmentRef.get()?.requestPermissions()
    }

    override fun getPermissionsArray(): Array<String> {
        return parentFragmentRef.get()?.getPermissionsArray() ?: emptyArray()
    }

    override fun onPermissionsGranted() {
        parentFragmentRef.get()?.removeCurrentFragment()
    }
}