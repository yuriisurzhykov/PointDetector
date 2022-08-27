package com.yuriisurzhykov.pointdetector.presentation.points.create

import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointsdetector.uicomponents.WeekDay

sealed class PointCreateState {
    class SearchState(val suggestedList: List<Point>) : PointCreateState()
    class PointSelectedState(val point: Point) : PointCreateState()
    class SavedPointState(val point: Point, val workingHours: List<WeekDay>) : PointCreateState()
}
