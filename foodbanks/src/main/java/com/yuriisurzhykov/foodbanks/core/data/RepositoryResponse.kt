package com.yuriisurzhykov.foodbanks.core.data

interface RepositoryResponse<T : Any> {

    class Success<T : Any>(val items: T) : RepositoryResponse<T>

    class NoNetwork<T : Any>(val cacheItems: T) : RepositoryResponse<T>

    class NetworkError<T : Any>(val cacheItems: T) : RepositoryResponse<T>
}