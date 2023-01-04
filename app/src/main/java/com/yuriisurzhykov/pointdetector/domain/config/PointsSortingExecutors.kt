package com.yuriisurzhykov.pointdetector.domain.config

import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi

class PointsSortByNameExecutor : SortingTypeExecutor<PointUi> {
    override suspend fun performSort(items: Collection<PointUi>): Collection<PointUi> {
        return items.sortedBy { it.placeName }
    }
}

class PointsSortByDistanceExecutor : SortingTypeExecutor<PointUi> {
    override suspend fun performSort(items: Collection<PointUi>): Collection<PointUi> {
        return items.sortedBy { it.distanceDouble }
    }
}