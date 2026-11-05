package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.foodbanks.domain.points.Point
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import javax.inject.Inject

class PointsToUiListMapper @Inject constructor(
    private val mapper: SuspendMapper<Point, PointUi>
) : SuspendMapper<List<Point>, List<PointUi>> {

    override suspend fun map(from: List<Point>): List<PointUi> = from.map { mapper.map(it) }
}
