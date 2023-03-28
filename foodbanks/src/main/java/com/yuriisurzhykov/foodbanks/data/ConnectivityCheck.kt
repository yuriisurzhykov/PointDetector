package com.yuriisurzhykov.foodbanks.data

interface ConnectivityCheck {
    fun isNetworkAvailable(): Boolean
}