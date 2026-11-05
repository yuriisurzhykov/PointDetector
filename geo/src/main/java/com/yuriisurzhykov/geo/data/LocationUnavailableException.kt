package com.yuriisurzhykov.geo.data

class LocationUnavailableException(reason: String, cause: Throwable? = null) :
    RuntimeException("User's location is unavailable: $reason", cause)
