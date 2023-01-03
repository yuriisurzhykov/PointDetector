package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import javax.inject.Inject

class PointUiToDomainMapper @Inject constructor() : Mapper<PointUi, Point> {
    override fun map(from: PointUi): Point {
        return Point(from.address, from.coordinates, from.placeName, from.workingHours)
    }
}