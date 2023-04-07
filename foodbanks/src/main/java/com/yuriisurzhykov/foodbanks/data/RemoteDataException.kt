package com.yuriisurzhykov.foodbanks.data

import java.lang.Exception

class RemoteDataException(exception: Exception) :
    Exception("Failed to fetch remote data! Got exception: $exception")