package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi

@Suppress("unused")
sealed class PointsSortingTypeService(val sortingType: String) {

    abstract fun sort(points: List<PointUi>): List<PointUi>

    data object SortByDistance : PointsSortingTypeService("distance") {
        override fun sort(points: List<PointUi>) = points.sortedBy { it.distanceDouble }
    }

    data object SortByCreatedDate : PointsSortingTypeService("creation_date") {
        override fun sort(points: List<PointUi>) = points
    }

}