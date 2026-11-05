package com.yuriisurzhykov.foodbanks.domain.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck

/**
 * Asks the system every time instead of remembering the state from a callback: the callback
 * is called later than the first request for data is made, so the first answer would be wrong.
 */
class AndroidConnectivityCheck(context: Context) : ConnectivityCheck {

    private val manager =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    override fun isNetworkAvailable(): Boolean {
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
