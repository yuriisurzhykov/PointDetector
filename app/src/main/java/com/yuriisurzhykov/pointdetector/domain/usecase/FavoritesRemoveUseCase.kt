package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.foodbanks.domain.favorites.FavoritesUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.favorites.FavoritesRemove
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface FavoritesRemoveUseCase : FavoritesRemove<PointUi> {

    abstract class Abstract constructor(
        private val favoritesUseCase: FavoritesUseCase
    ) : FavoritesRemoveUseCase {

        override fun remove(item: PointUi) {
            runBlocking { favoritesUseCase.unmarkFavorite(item.id) }
        }
    }

    class Base @Inject constructor(favoritesUseCase: FavoritesUseCase) :
        Abstract(favoritesUseCase)
}
