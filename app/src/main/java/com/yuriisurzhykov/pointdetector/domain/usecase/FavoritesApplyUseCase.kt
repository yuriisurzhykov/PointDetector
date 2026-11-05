package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.foodbanks.domain.favorites.FavoritesUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.favorites.FavoritesApply
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface FavoritesApplyUseCase : FavoritesApply<PointUi> {

    /**
     * Switches the favorite state of the point.
     */
    abstract class Abstract constructor(
        private val favoritesUseCase: FavoritesUseCase
    ) : FavoritesApplyUseCase {

        override fun applyFavorite(item: PointUi) {
            runBlocking {
                if (item.favoriteState == FavoriteState.FavoriteEnabled) {
                    favoritesUseCase.unmarkFavorite(item.id)
                } else {
                    favoritesUseCase.markFavorite(item.id)
                }
            }
        }
    }

    class Base @Inject constructor(favoritesUseCase: FavoritesUseCase) :
        Abstract(favoritesUseCase)
}
