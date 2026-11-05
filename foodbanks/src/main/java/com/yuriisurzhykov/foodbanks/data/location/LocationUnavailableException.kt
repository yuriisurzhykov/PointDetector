package com.yuriisurzhykov.foodbanks.data.location

class LocationUnavailableException(reason: String, cause: Throwable? = null) :
    RuntimeException("User's location is unavailable: $reason", cause)
