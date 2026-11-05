package com.yuriisurzhykov.foodbanks.domain.city

class CityNotDetectedException(reason: String, cause: Throwable? = null) :
    RuntimeException("Cannot detect user's city: $reason", cause)
