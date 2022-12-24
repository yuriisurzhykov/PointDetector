package com.yuriisurzhykov.pointdetector.presentation.points.create

import com.yuriisurzhykov.pointdetector.domain.entities.Point

sealed class PointCreateState {
    object SearchState : PointCreateState()
    class PointSelectedState(val point: Point) : PointCreateState()
    object SavedPointState : PointCreateState()
}
