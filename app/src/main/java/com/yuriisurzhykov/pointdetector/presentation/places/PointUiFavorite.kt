package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi

/** The same point with the opposite favorite state. */
fun PointUi.toggledFavorite(): PointUi = copy(
    favoriteState = if (favoriteState == FavoriteState.FavoriteEnabled) {
        FavoriteState.FavoriteDisabled
    } else {
        FavoriteState.FavoriteEnabled
    },
)
