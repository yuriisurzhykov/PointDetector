package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.domain.hours.PlaceStanding
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi

/** A point together with where it stands now. */
class PlaceRow(val point: PointUi, val standing: PlaceStanding) {
    val favorite: Boolean get() = point.favoriteState == FavoriteState.FavoriteEnabled
}
