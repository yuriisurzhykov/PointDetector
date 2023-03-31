package com.yuriisurzhykov.foodbanks.data.prefs

class NoPreferenceException(private val preferenceName: String) :
    RuntimeException("There is no selected preference: $preferenceName") {

    override fun toString(): String {
        return "NoPreferenceException(preferenceName='$preferenceName')"
    }
}