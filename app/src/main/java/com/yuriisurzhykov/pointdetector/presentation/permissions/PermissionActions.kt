package com.yuriisurzhykov.pointdetector.presentation.permissions

/** What the person can do on a permission screen. The screen only reports; the host decides. */
interface PermissionActions {

    /** Open the system dialog. */
    fun onGrant()

    /** Open the settings of the app. */
    fun onOpenSettings()

    /** Go on without the permission. */
    fun onContinue()
}
