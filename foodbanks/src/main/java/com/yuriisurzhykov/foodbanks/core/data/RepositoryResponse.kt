package com.yuriisurzhykov.foodbanks.core.data

interface RepositoryResponse<T : Any> {

    class Success<T : Any>(private val items: T) : RepositoryResponse<T>

    class NoNetwork<T : Any>(private val cacheItems: T) : RepositoryResponse<T>

    class NetworkError<T : Any>(private val cacheItems: T) : RepositoryResponse<T>
}