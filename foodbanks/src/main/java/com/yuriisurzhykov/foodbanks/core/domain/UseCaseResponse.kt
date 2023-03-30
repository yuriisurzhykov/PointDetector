package com.yuriisurzhykov.foodbanks.core.domain

interface UseCaseResponse<T : Any> {
    class Success<T : Any>(val data: T) : UseCaseResponse<T>
    class NetworkError<T : Any>(val items: T) : UseCaseResponse<T>
    class NoNetwork<T : Any>(val data: T) : UseCaseResponse<T>
}