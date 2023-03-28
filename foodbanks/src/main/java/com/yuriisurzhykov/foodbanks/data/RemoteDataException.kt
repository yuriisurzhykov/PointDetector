package com.yuriisurzhykov.foodbanks.data

import java.lang.Exception

class RemoteDataException(private val exception: Exception) :
    Exception("Failed to fetch remote data! Got exception: $exception")