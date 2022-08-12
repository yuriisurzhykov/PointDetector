package com.yuriisurzhykov.pointdetector.domain.services

import android.location.Location
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object LocationManager {

    private val locationLiveData = MutableLiveData<Location>()

    fun observeLocation(observer: Observer<Location>) {
        locationLiveData.observeForever(observer)
    }

    fun removeObserver(observer: Observer<Location>) {
        locationLiveData.removeObserver(observer)
    }

    fun getCurrentLocation(): Location? {
        return locationLiveData.value
    }

    fun setLocation(location: Location) {
        if (Looper.getMainLooper().isCurrentThread) {
            locationLiveData.value = location
        } else {
            locationLiveData.postValue(location)
        }
    }

}