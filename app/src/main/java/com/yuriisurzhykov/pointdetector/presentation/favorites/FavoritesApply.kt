package com.yuriisurzhykov.pointdetector.presentation.favorites

fun interface FavoritesApply<T> {

    fun applyFavorite(item: T)
}