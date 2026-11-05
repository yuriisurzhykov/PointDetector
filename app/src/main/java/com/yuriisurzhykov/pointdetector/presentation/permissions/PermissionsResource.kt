package com.yuriisurzhykov.pointdetector.presentation.permissions

interface PermissionsResource {
    fun getPermissionsArray(): Array<String>
    fun onPermissionsGranted()
    fun requestPermissions()
}
