package com.yuriisurzhykov.foodbanks.domain.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck

class AndroidConnectivityCheck(context: Context, private val networkRequest: NetworkRequest) :
    ConnectivityCheck,
    ConnectivityManager.NetworkCallback() {

    private var isNetworkAvailable: Boolean = false

    private val manager =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    init {
        manager.registerNetworkCallback(networkRequest, this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        isNetworkAvailable = false
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        isNetworkAvailable = false
    }

    override fun onUnavailable() {
        super.onUnavailable()
        isNetworkAvailable = false
    }

    override fun isNetworkAvailable(): Boolean = isNetworkAvailable
}