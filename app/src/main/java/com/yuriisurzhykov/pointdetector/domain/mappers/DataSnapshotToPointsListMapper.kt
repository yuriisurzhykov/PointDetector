package com.yuriisurzhykov.pointdetector.domain.mappers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

class DataSnapshotToPointsListMapper @Inject constructor() : Mapper<DataSnapshot, List<Point>> {
    override fun map(from: DataSnapshot): List<Point> {
        return from.getValue(object : GenericTypeIndicator<java.util.ArrayList<Point>>() {})
            .orEmpty()
    }
}